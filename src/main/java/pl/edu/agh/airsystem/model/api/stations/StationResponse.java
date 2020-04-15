package pl.edu.agh.airsystem.model.api.stations;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class StationResponse {
    private final long id;
    private final String name;
    private final Address address;
    private final Location location;
    private final Map<String, SensorResponse> sensors;

    public StationResponse(Station station, MeasurementQuery measurementQuery) {
        this.id = station.getId();
        this.name = station.getName();
        this.address = station.getAddress();
        this.location = station.getLocation();

        this.sensors = station.getSensors().stream()
                .collect(Collectors.toMap(Sensor::getId, e -> new SensorResponse(e, measurementQuery)));

    }

}
