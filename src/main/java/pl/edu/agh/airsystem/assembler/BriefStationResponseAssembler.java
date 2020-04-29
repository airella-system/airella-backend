package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.stations.BriefStationResponse;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.util.AirStatusService;

@Component
@AllArgsConstructor
public class BriefStationResponseAssembler {
    private SensorResponseAssembler sensorResponseAssembler;
    private AirStatusService airStatusService;

    public BriefStationResponse assemble(Station station) {
        return new BriefStationResponse(
                station.getId(),
                station.getName(),
                station.getAddress(),
                station.getLocation(),
                airStatusService.calculateAirQualityIndex(station));
    }

}
