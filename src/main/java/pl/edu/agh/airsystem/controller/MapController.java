package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.airsystem.model.api.DataResponse;
import pl.edu.agh.airsystem.service.SensorInterpolationService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/map")
public class MapController {
    private final SensorInterpolationService sensorInterpolationService;

    @GetMapping("/at")
    public ResponseEntity<DataResponse> getStations(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        return sensorInterpolationService.getResponse(latitude, longitude);
    }

}