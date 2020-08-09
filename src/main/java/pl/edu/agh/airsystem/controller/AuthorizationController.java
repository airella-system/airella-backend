package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.api.authorization.*;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.service.AuthorizationService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<? extends Response> login(
            @RequestBody LoginRequest authenticationRequest) {
        return authorizationService.login(authenticationRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<? extends Response> refreshToken(
            @RequestBody RefreshTokenRequest authenticationRequest) {
        return authorizationService.refreshToken(authenticationRequest);
    }

    @PostMapping("/register-user")
    public ResponseEntity<? extends Response> registerUser(
            @RequestBody RegisterUserRequest registerUserRequest) {
        return authorizationService.registerUser(registerUserRequest);
    }

    @GetMapping("/activate-user")
    public ResponseEntity<? extends Response> activateUser(
            @RequestParam String activateString) {
        return authorizationService.activateUser(activateString);
    }

    @PostMapping("/register-station")
    public ResponseEntity<? extends Response> registerStation(
            @RequestBody RegisterStationRequest registerStationRequest) {
        return authorizationService.registerStation(registerStationRequest);
    }

}