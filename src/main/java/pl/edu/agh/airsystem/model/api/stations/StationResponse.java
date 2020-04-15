package pl.edu.agh.airsystem.model.api.stations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class StationResponse {
    private final long id;
    private final String name;
    private final Address address;
    private final Location location;
    private final Map<String, SensorResponse> sensors;
}
