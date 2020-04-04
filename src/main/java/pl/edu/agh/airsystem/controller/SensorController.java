package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.api.sensors.BriefSensorResponse;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.service.SensorService;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class SensorController {
    private final SensorService sensorService;

    @GetMapping("{stationId}/sensors")
    public ResponseEntity<Map<String, BriefSensorResponse>> getSensors(
            @PathVariable(value = "stationId") Long stationId) {
        return sensorService.getSensors(stationId);
    }

    @GetMapping("{stationId}/sensors/{sensorId}")
    public ResponseEntity<BriefSensorResponse> getSensor(
            @PathVariable(value = "stationId") Long stationId,
            @PathVariable(value = "sensorId") String sensorId) {
        return sensorService.getSensor(stationId, sensorId);
    }

    @PostMapping("{stationId}/sensors")
    public ResponseEntity<?> addSensor(
            @PathVariable(value = "stationId") Long stationId,
            @RequestBody NewSensorRequest newSensor) {
        return sensorService.addSensor(stationId, newSensor);
    }



}
