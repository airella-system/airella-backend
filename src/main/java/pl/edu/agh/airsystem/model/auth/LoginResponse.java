package pl.edu.agh.airsystem.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.security.model.JWTToken;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final JWTToken accessToken;
    private final String refreshToken;
}