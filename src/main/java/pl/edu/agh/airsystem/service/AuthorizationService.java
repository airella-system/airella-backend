package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.airsystem.exception.*;
import pl.edu.agh.airsystem.model.api.authorization.*;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.response.SuccessResponse;
import pl.edu.agh.airsystem.model.api.security.JWTToken;
import pl.edu.agh.airsystem.model.database.*;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.repository.*;
import pl.edu.agh.airsystem.security.util.JWTTokenUtil;

import javax.mail.MessagingException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private final AuthenticationManager authenticationManager;
    private final JWTTokenUtil jwtTokenUtil;
    private final ClientRepository clientRepository;
    private final UserClientRepository userClientRepository;
    private final UserClientStubRepository userClientStubRepository;
    private final StationClientRepository stationClientRepository;
    private final StationRepository stationRepository;
    private final EmailSenderService emailSenderService;
    private final ResourceFinder resourceFinder;

    public ResponseEntity<Response> login(LoginRequest authenticationRequest) {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserClient userClient = userClientRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(authenticationRequest.getEmail()));

        final JWTToken accessToken = jwtTokenUtil.generateAccessToken(userClient);

        return ResponseEntity.ok(DataResponse.of(new LoginResponse(
                accessToken,
                userClient.getRefreshToken(),
                userClient.getStationRegistrationToken())));
    }

    public ResponseEntity<Response> refreshToken(RefreshTokenRequest authenticationRequest) {
        String refreshToken = authenticationRequest.getRefreshToken();

        Optional<Client> client = clientRepository.findByRefreshToken(refreshToken);
        if (client.isEmpty()) {
            throw new WrongTokenException();
        }

        final JWTToken token = jwtTokenUtil.generateAccessToken(client.get());
        return ResponseEntity.ok(DataResponse.of(new RefreshTokenResponse(token)));
    }

    public ResponseEntity<Response> registerUser(RegisterUserRequest registerUserRequest) {
        if (userClientRepository.findByEmail(registerUserRequest.getEmail()).isPresent() ||
                userClientStubRepository.findByEmail(registerUserRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        UserClientStub userClientStub = new UserClientStub(registerUserRequest.getEmail(),
                new BCryptPasswordEncoder().encode(registerUserRequest.getPassword()),
                UUID.randomUUID().toString());

        try {
            emailSenderService.sendActivationString(userClientStub.getEmail(), userClientStub.getActivateString());
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new EmailServiceErrorException();
        }

        userClientStubRepository.save(userClientStub);
        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<? extends Response> activateUser(String activateString) {
        Optional<UserClientStub> userClientStub = userClientStubRepository.findByActivateString(
                activateString);
        if (userClientStub.isEmpty()) {
            throw new WrongActivateStringException();
        }

        UserClient userClient = new UserClient(userClientStub.get().getEmail(),
                userClientStub.get().getPasswordHash());

        userClientRepository.save(userClient);
        userClientStubRepository.delete(userClientStub.get());
        return ResponseEntity.ok(new SuccessResponse());
    }

    public static String generateStationId() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    public String generateUniqueStationId() {
        String id;
        do {
            id = generateStationId();
        } while (stationRepository.findById(id).isPresent());

        return id;
    }

    public ResponseEntity<Response> registerStation(RegisterStationRequest registerStationRequest) {
        Optional<UserClient> userClient = userClientRepository
                .findByStationRegistrationToken(registerStationRequest.getStationRegistrationToken());

        if (userClient.isEmpty()) {
            throw new WrongTokenException();
        }

        Station station = new Station();
        station.setId(generateUniqueStationId());
        station.setOwner(userClient.get());
        StationClient stationClient = new StationClient(station);
        station.setStationClient(stationClient);

        stationClientRepository.save(stationClient);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/stations/{id}")
                .buildAndExpand(station.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(DataResponse.of(
                        new RegisterStationResponse(String.valueOf(station.getId()), stationClient.getRefreshToken())));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    public StationClient checkAuthenticationAndGetStationClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof StationClient) {
            return (StationClient) principal;
        }
        throw new StationClientAuthenticationRequiredException();
    }

    public UserClient checkAuthenticationAndGetUserClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserClient) {
            return (UserClient) principal;
        }
        throw new UserClientAuthenticationRequiredException();
    }

    public Client checkAuthenticationAndGetClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof Client) {
            return (Client) principal;
        }
        throw new UserClientAuthenticationRequiredException();
    }

    public Client getClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof Client) {
            return (Client) principal;
        }
        return null;
    }

    private UserClient getUserClient(Client client) {
        UserClient userClient;
        if (client instanceof UserClient) {
            userClient = ((UserClient) client);
        } else if (client instanceof StationClient) {
            userClient = ((StationClient) client).getStation().getOwner();
        } else {
            throw new IllegalArgumentException("Unknown client type");
        }
        return userClient;
    }

    public void ensureClientHasStation(Client client, Station station) {
        UserClient userClient = getUserClient(client);

        if (userClient.getId() != station.getOwner().getId()) {
            throw new NotUsersStationException();
        }
    }

    public boolean checkIfClientHasStatistic(Client client, Statistic statistic) {
        UserClient userClient = getUserClient(client);

        return userClient.getId() == statistic.getStation().getOwner().getId();
    }

    public void ensureClientHasStatistic(Client client, Statistic statistic) {
        if (!checkIfClientHasStatistic(client, statistic)) {
            throw new NotUsersStationException();
        }
    }
}

