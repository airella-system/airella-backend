package pl.edu.agh.airsystem.model.api.stations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;

@Getter
@Setter
@AllArgsConstructor
public class BriefStationResponse {
    private final String id;
    private final String name;
    private final Address address;
    private final Location location;
    private final Double aqi;
}
