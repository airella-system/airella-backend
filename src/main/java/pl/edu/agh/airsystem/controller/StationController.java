package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.stations.BriefStationResponse;
import pl.edu.agh.airsystem.model.stations.StationResponse;
import pl.edu.agh.airsystem.service.StationService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<List<BriefStationResponse>> getStations() {
        return stationService.getStations();
    }

    @ResponseBody
    @GetMapping("{stationId}")
    public ResponseEntity<StationResponse> getStations(
            @PathVariable(value = "stationId") Long stationId) {
        return stationService.getStations(stationId);
    }

}