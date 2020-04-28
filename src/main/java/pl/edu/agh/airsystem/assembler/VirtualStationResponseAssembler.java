package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.mappoint.VirtualSensorResponse;
import pl.edu.agh.airsystem.model.api.mappoint.VirtualStationResponse;
import pl.edu.agh.airsystem.model.database.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.edu.agh.airsystem.util.AirStatusUtils.calculateAirQualityIndex;

@Component
@AllArgsConstructor
public class VirtualStationResponseAssembler {
    private VirtualSensorResponseAssembler virtualSensorResponseAssembler;

    public VirtualStationResponse assemble(Station station) {
        List<VirtualSensorResponse> sensorResponses = station.getSensors().stream()
                .map(e -> virtualSensorResponseAssembler.assemble(e))
                .collect(toList());

        return new VirtualStationResponse(
                sensorResponses,
                calculateAirQualityIndex(station));
    }

}
