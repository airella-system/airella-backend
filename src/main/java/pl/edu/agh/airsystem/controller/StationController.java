package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.api.stations.BriefStationResponse;
import pl.edu.agh.airsystem.model.api.stations.LocationChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.StationResponse;
import pl.edu.agh.airsystem.service.StationService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;

    @GetMapping("")
    public ResponseEntity<List<BriefStationResponse>> getStations() {
        return stationService.getStations();
    }

    @GetMapping("{stationId}")
    public ResponseEntity<StationResponse> getStation(
            @PathVariable(value = "stationId") Long stationId) {
        return stationService.getStation(stationId);
    }

    @PutMapping("{stationId}/location")
    public ResponseEntity<?> setLocation(
            @PathVariable(value = "stationId") Long stationId,
            @RequestBody LocationChangeRequest locationChangeRequest) {
        return stationService.setStationLocation(stationId, locationChangeRequest);
    }

}