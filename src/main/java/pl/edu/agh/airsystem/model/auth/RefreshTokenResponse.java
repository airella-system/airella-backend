package pl.edu.agh.airsystem.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.security.model.JWTToken;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private JWTToken accessToken;
}