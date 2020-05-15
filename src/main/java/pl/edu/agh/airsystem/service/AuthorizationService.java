package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.airsystem.exception.*;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.authorization.*;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.response.SuccessResponse;
import pl.edu.agh.airsystem.model.api.security.JWTToken;
import pl.edu.agh.airsystem.model.database.*;
import pl.edu.agh.airsystem.repository.*;
import pl.edu.agh.airsystem.security.util.JWTTokenUtil;

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

    public ResponseEntity<Response> login(LoginRequest authenticationRequest) {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserClient userClient = userClientRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(authenticationRequest.getEmail()));

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
        if (userClientRepository.findByEmail(registerUserRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        UserClientStub userClientStub = new UserClientStub(registerUserRequest.getEmail(),
                new BCryptPasswordEncoder().encode(registerUserRequest.getPassword()),
                UUID.randomUUID().toString());

        userClientStubRepository.save(userClientStub);
        emailSenderService.sendActivationString(userClientStub.getEmail(), userClientStub.getActivateString());
        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<? extends Response> activateUser(ActivateUserRequest activateUserRequest) {
        Optional<UserClientStub> userClientStub = userClientStubRepository.findByActivateString(
                activateUserRequest.getActivateString());
        if (userClientStub.isEmpty()) {
            throw new WrongActivateStringException();
        }

        UserClient userClient = new UserClient(userClientStub.get().getEmail(),
                userClientStub.get().getPasswordHash());

        userClientRepository.save(userClient);
        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<Response> registerStation(RegisterStationRequest registerStationRequest) {
        Optional<UserClient> userClient = userClientRepository
                .findByStationRegistrationToken(registerStationRequest.getStationRegistrationToken());

        if (userClient.isEmpty()) {
            throw new WrongTokenException();
        }

        Station station = new Station();
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
                        new RegisterStationResponse(stationClient.getRefreshToken())));
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


}

