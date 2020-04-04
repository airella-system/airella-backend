package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.airsystem.model.api.stations.StationResponse;
import pl.edu.agh.airsystem.service.SearchService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/map")
    public ResponseEntity<List<StationResponse>> getStations(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        return searchService.getStations(latitude, longitude, radius);
    }

}