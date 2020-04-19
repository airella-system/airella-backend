package pl.edu.agh.airsystem.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.airsystem.model.api.DataResponse;
import pl.edu.agh.airsystem.model.api.authorization.LoginRequest;
import pl.edu.agh.airsystem.model.api.authorization.RefreshTokenRequest;
import pl.edu.agh.airsystem.model.api.authorization.RegisterStationRequest;
import pl.edu.agh.airsystem.model.api.authorization.RegisterUserRequest;
import pl.edu.agh.airsystem.service.AuthorizationService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<DataResponse> login(
            @RequestBody LoginRequest authenticationRequest) {
        return authorizationService.login(authenticationRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<DataResponse> refreshToken(
            @RequestBody RefreshTokenRequest authenticationRequest) {
        return authorizationService.refreshToken(authenticationRequest);
    }

    @PostMapping("/register-user")
    public ResponseEntity<DataResponse> registerUser(
            @RequestBody RegisterUserRequest registerUserRequest) {
        return authorizationService.registerUser(registerUserRequest);
    }

    @PostMapping("/register-station")
    public ResponseEntity<DataResponse> registerStation(
            @RequestBody RegisterStationRequest registerStationRequest) {
        return authorizationService.registerStation(registerStationRequest);
    }

}