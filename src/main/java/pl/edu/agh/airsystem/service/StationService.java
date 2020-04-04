package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.stations.BriefStationResponse;
import pl.edu.agh.airsystem.model.stations.StationResponse;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    public ResponseEntity<List<BriefStationResponse>> getStations() {
        List<BriefStationResponse> response = new ArrayList<>();
        stationRepository.findAll().forEach(station -> response.add(new BriefStationResponse(station)));
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<StationResponse> getStations(Long stationId) {
        return ResponseEntity.ok()
                .body(new StationResponse(stationRepository.findById(stationId).get()));
    }

}

