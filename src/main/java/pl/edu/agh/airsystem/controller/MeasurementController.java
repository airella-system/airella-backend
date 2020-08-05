package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.api.measurement.NewMeasurementRequest;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.service.MeasurementService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class MeasurementController {
    private final MeasurementService measurementService;

    @PostMapping("{stationId}/sensors/{sensorId}/measurements")
    public ResponseEntity<? extends Response> addMeasurement(
            @PathVariable(value = "stationId") String stationId,
            @PathVariable(value = "sensorId") String sensorId,
            @RequestBody NewMeasurementRequest newMeasurementRequest) {
        return measurementService.addMeasurement(stationId, sensorId, newMeasurementRequest);
    }

}
