package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.airsystem.model.api.authorization.*;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.service.RegisterLoginService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthorizationController {
    private final RegisterLoginService registerLoginService;

    @PostMapping("/login")
    public ResponseEntity<? extends Response> login(
            @RequestBody LoginRequest authenticationRequest) {
        return registerLoginService.login(authenticationRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<? extends Response> refreshToken(
            @RequestBody RefreshTokenRequest authenticationRequest) {
        return registerLoginService.refreshToken(authenticationRequest);
    }

    @PostMapping("/register-user")
    public ResponseEntity<? extends Response> registerUser(
            @RequestBody RegisterUserRequest registerUserRequest) {
        return registerLoginService.registerUser(registerUserRequest);
    }

    @GetMapping("/activate-user")
    public ResponseEntity<? extends Response> activateUser(
            @RequestParam String email,
            @RequestParam String activationCode) {
        return registerLoginService.activateUser(email, activationCode);
    }

    @PostMapping("/register-station")
    public ResponseEntity<? extends Response> registerStation(
            @RequestBody RegisterStationRequest registerStationRequest) {
        return registerLoginService.registerStation(registerStationRequest);
    }

    @PostMapping("/delete-user")
    public ResponseEntity<? extends Response> registerStation(
            @RequestBody DeleteUserRequest deleteUserRequest) {
        return registerLoginService.deleteUser(deleteUserRequest);
    }

}
