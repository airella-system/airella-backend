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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.airsystem.exception.*;
import pl.edu.agh.airsystem.filters.JWTAuthorizationFilter;
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
@Transactional
@AllArgsConstructor
public class RegisterLoginService {
    private final AuthenticationManager authenticationManager;
    private final JWTTokenUtil jwtTokenUtil;
    private final ClientRepository clientRepository;
    private final UserClientRepository userClientRepository;
    private final UserClientStubRepository userClientStubRepository;
    private final StationClientRepository stationClientRepository;
    private final StationRepository stationRepository;
    private final EmailSenderService emailSenderService;
    private final StationService stationService;
    private final AuthorizationService authorizationService;

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

        if (emailSenderService.isEmailActivationEnabled()) {
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
        } else {
            UserClient newUserClient = new UserClient(registerUserRequest.getEmail(),
                    new BCryptPasswordEncoder().encode(registerUserRequest.getPassword()));

            userClientRepository.save(newUserClient);
        }

        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<? extends Response> activateUser(String email, String activateString) {
        Optional<UserClient> userClient = userClientRepository.findByEmail(email);
        if (userClient.isPresent()) {
            throw new AlreadyActivatedException();
        }

        Optional<UserClientStub> userClientStub = userClientStubRepository.findByEmail(
                email);

        if (userClientStub.isEmpty()) {
            throw new UnknownUserEmailException();
        }

        if (!userClientStub.get().getActivateString().equals(activateString)) {
            throw new WrongActivateStringException();
        }

        UserClient newUserClient = new UserClient(userClientStub.get().getEmail(),
                userClientStub.get().getPasswordHash());

        userClientRepository.save(newUserClient);
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

        if (registerStationRequest.getAdditionalQuery() != null) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(stationClient,
                            null, JWTAuthorizationFilter.getGrantedAuthorities(stationClient)));
            stationService.makeComplexQuery(station.getId(), registerStationRequest.getAdditionalQuery());
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/stations/{id}")
                .buildAndExpand(station.getId())
                .toUri();

        final JWTToken accessToken = jwtTokenUtil.generateAccessToken(stationClient);

        return ResponseEntity
                .created(uri)
                .body(DataResponse.of(
                        new RegisterStationResponse(
                                String.valueOf(station.getId()),
                                stationClient.getRefreshToken(),
                                accessToken)));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    public ResponseEntity<? extends Response> deleteUser(DeleteUserRequest deleteUserRequest) {
        Client client = authorizationService.checkAuthenticationAndGetClient();

        if (!client.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new AdministratorRightsRequiredException();
        }

        Optional<UserClientStub> userClientStub = userClientStubRepository
                .findByEmail(deleteUserRequest.getEmail());

        Optional<UserClient> userClient = userClientRepository
                .findByEmail(deleteUserRequest.getEmail());


        if (userClientStub.isPresent()) {
            userClientStubRepository.delete(userClientStub.get());
            return ResponseEntity.ok(new SuccessResponse());
        } else if (userClient.isPresent()) {
            userClient.get().getStations().forEach(station ->
                    stationService.deleteStation(station.getId()));
            userClientRepository.delete(userClient.get());
            return ResponseEntity.ok(new SuccessResponse());
        } else {
            throw new EmailNotFoundException(deleteUserRequest.getEmail());
        }
    }
}

