package pl.edu.agh.airsystem.model.api.stations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StationResponse {
    private final long id;
    private final String name;
    private final Address address;
    private final Location location;
    private final List<SensorResponse> sensors;
    private Double caqi;
}
