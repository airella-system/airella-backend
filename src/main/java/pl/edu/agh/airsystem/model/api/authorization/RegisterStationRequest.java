package pl.edu.agh.airsystem.model.api.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.stations.complexapi.ComplexQueryRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStationRequest {
    private String stationRegistrationToken;
    private ComplexQueryRequest additionalQuery;
}
