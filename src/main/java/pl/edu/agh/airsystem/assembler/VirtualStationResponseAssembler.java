package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.mappoint.VirtualSensorResponse;
import pl.edu.agh.airsystem.model.api.mappoint.VirtualStationResponse;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.util.AirStatusService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class VirtualStationResponseAssembler {
    private VirtualSensorResponseAssembler virtualSensorResponseAssembler;
    private AirStatusService airStatusService;

    public VirtualStationResponse assemble(Station station) {
        List<VirtualSensorResponse> sensorResponses = station.getSensors().stream()
                .map(e -> virtualSensorResponseAssembler.assemble(e))
                .collect(toList());

        return new VirtualStationResponse(
                sensorResponses,
                airStatusService.calculateAirQualityIndex(station));
    }

}
