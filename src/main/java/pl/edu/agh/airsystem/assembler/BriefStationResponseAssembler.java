package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.stations.BriefStationResponse;
import pl.edu.agh.airsystem.model.database.Station;

import static pl.edu.agh.airsystem.util.AirStatusUtils.calculateAirStatus;

@Component
@AllArgsConstructor
public class BriefStationResponseAssembler {
    private SensorResponseAssembler sensorResponseAssembler;

    public BriefStationResponse assemble(Station station) {
        return new BriefStationResponse(
                station.getId(),
                station.getName(),
                station.getAddress(),
                station.getLocation(),
                calculateAirStatus(station));
    }

}
