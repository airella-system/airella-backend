package pl.edu.agh.airsystem.model.api.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.security.JWTToken;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStationResponse {
    private String id;
    private String refreshToken;
    private JWTToken accessToken;
}
