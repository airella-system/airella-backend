package pl.edu.agh.airsystem.model.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.security.JWTToken;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final JWTToken accessToken;
    private final String refreshToken;
    private final String stationRegistrationToken;
}