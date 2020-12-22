package pl.edu.agh.airsystem.model.api.stations.complexapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.stations.AddressChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.LocationChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.NameChangeRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplexQuerySetRequest {
    private NameChangeRequest name;
    private AddressChangeRequest address;
    private LocationChangeRequest location;
}
