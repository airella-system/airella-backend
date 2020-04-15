package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.converter.MeasurementQueryConverter;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryRequest;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.service.SensorService;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class SensorController {
    private final SensorService sensorService;

    @GetMapping("{stationId}/sensors")
    public ResponseEntity<Map<String, SensorResponse>> getSensors(
            @PathVariable(value = "stationId") Long stationId,
            MeasurementQueryRequest measurementQueryRequest) {
        return sensorService.getSensors(stationId,
                MeasurementQueryConverter.of(measurementQueryRequest));
    }

    @GetMapping("{stationId}/sensors/{sensorId}")
    public ResponseEntity<SensorResponse> getSensor(
            @PathVariable(value = "stationId") Long stationId,
            @PathVariable(value = "sensorId") String sensorId,
            MeasurementQueryRequest measurementQueryRequest) {
        return sensorService.getSensor(stationId, sensorId,
                MeasurementQueryConverter.of(measurementQueryRequest));
    }

    @PostMapping("{stationId}/sensors")
    public ResponseEntity<?> addSensor(
            @PathVariable(value = "stationId") Long stationId,
            @RequestBody NewSensorRequest newSensor) {
        return sensorService.addSensor(stationId, newSensor);
    }


}
