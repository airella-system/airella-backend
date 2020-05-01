package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.airsystem.converter.MeasurementQueryConverter;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.service.SearchService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;
    private final MeasurementQueryConverter measurementQueryConverter;

    @GetMapping("/map")
    public ResponseEntity<? extends Response> getStations(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        return searchService.getStations(latitude, longitude, radius);
    }

}