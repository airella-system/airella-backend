package pl.edu.agh.airsystem.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.authorization.*;
import pl.edu.agh.airsystem.model.database.Client;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.StationClient;
import pl.edu.agh.airsystem.model.database.UserClient;
import pl.edu.agh.airsystem.model.error.ErrorBody;
import pl.edu.agh.airsystem.model.security.JWTToken;
import pl.edu.agh.airsystem.repository.ClientRepository;
import pl.edu.agh.airsystem.repository.StationClientRepository;
import pl.edu.agh.airsystem.repository.StationRepository;
import pl.edu.agh.airsystem.repository.UserClientRepository;
import pl.edu.agh.airsystem.security.util.JWTTokenUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final JWTTokenUtil jwtTokenUtil;
    private final ClientRepository clientRepository;
    private final UserClientRepository userClientRepository;
    private final StationClientRepository stationClientRepository;
    private final StationRepository stationRepository;


    @GetMapping("/test")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserClient) {
            return "You are user";
        } else if (principal instanceof StationClient) {
            return "You are station";
        }
        return "You are... I don't know ;(";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserClient userClient = userClientRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + authenticationRequest.getUsername() + " not found"));

        final JWTToken accessToken = jwtTokenUtil.generateAccessToken(userClient);

        return ResponseEntity.ok(new LoginResponse(
                accessToken,
                userClient.getRefreshToken(),
                userClient.getStationRegistrationToken()));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest authenticationRequest) {
        String refreshToken = authenticationRequest.getRefreshToken();

        Optional<Client> client = clientRepository.findByRefreshToken(refreshToken);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ErrorBody("WRONG_TOKEN",
                            "This refresh token is wrong"));
        }

        final JWTToken token = jwtTokenUtil.generateAccessToken(client.get());
        return ResponseEntity.ok(new RefreshTokenResponse(token));
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        UserClient userClient = new UserClient(registerUserRequest.getUsername(),
                new BCryptPasswordEncoder().encode(registerUserRequest.getPassword()));

        if (userClientRepository.findByUsername(registerUserRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    new ErrorBody("USERNAME_TAKEN",
                            "This username is already taken"));
        }

        userClientRepository.save(userClient);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register-station")
    public ResponseEntity<?> registerStation(@RequestBody RegisterStationRequest registerStationRequest) {
        Optional<UserClient> userClient = userClientRepository
                .findByStationRegistrationToken(registerStationRequest.getStationRegistrationToken());

        if (userClient.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ErrorBody("WRONG_TOKEN",
                            "This registration token is wrong."));
        }

        Station station = new Station();
        station.setOwner(userClient.get());
        StationClient stationClient = new StationClient(station);

        stationRepository.save(station);
        stationClientRepository.save(stationClient);

        return ResponseEntity.ok(new RegisterStationResponse(stationClient.getRefreshToken()));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}