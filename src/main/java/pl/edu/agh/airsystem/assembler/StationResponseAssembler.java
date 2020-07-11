package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.api.stations.StationResponse;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.service.SensorService;
import pl.edu.agh.airsystem.util.AirStatusService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class StationResponseAssembler {
    private SensorResponseAssembler sensorResponseAssembler;
    private AirStatusService airStatusService;

    public StationResponse assemble(Station station, MeasurementQuery measurementQuery) {
        List<SensorResponse> sensorResponses = station.getSensors().stream()
                .filter(sensor -> SensorService.filterSensorType(sensor, measurementQuery.getTypes()))
                .map(sensor -> sensorResponseAssembler.assemble(sensor, measurementQuery))
                .collect(toList());

        return new StationResponse(
                String.valueOf(station.getId()),
                station.getName(),
                station.getAddress(),
                station.getLocation(),
                sensorResponses,
                airStatusService.calculateAirQualityIndex(station));
    }

}
