package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.stations.BriefStationResponse;
import pl.edu.agh.airsystem.model.stations.StationResponse;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stations")
@AllArgsConstructor
public class StationController {
    private final StationRepository stationRepository;

    @GetMapping("")
    @ResponseBody
    public List<BriefStationResponse> getStations() {
        List<BriefStationResponse> response = new ArrayList<>();
        stationRepository.findAll().forEach(station -> response.add(new BriefStationResponse(station)));
        return response;
    }

    @GetMapping("{stationId}")
    @ResponseBody
    public StationResponse getStations(@PathVariable(value = "stationId") Long stationId) {
        return new StationResponse(stationRepository.findById(stationId).get());
    }

}