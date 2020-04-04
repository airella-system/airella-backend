package pl.edu.agh.airsystem.model.api.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.api.security.JWTToken;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final JWTToken accessToken;
    private final String refreshToken;
    private final String stationRegistrationToken;
}