package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.api.stations.StationResponse;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;

import java.util.Map;
import java.util.stream.Collectors;

import static pl.edu.agh.airsystem.util.AirStatusUtils.calculateAirStatus;

@Component
@AllArgsConstructor
public class StationResponseAssembler {
    private SensorResponseAssembler sensorResponseAssembler;

    public StationResponse assemble(Station station, MeasurementQuery measurementQuery) {
        Map<String, SensorResponse> sensors = station.getSensors().stream()
                .collect(Collectors.toMap(Sensor::getId, e -> sensorResponseAssembler.assemble(e, measurementQuery)));

        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getAddress(),
                station.getLocation(),
                sensors,
                calculateAirStatus(station));
    }

}
