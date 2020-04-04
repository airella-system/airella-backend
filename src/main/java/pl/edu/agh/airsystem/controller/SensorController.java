package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.model.sensors.NewSensorResponse;
import pl.edu.agh.airsystem.model.stations.BriefSensorResponse;
import pl.edu.agh.airsystem.service.SensorService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class SensorController {
    private final SensorService sensorService;

    @GetMapping("{stationId}/sensors")
    public ResponseEntity<List<BriefSensorResponse>> getSensors(
            @PathVariable(value = "stationId") Long stationId) {
        return sensorService.getSensors(stationId);
    }

    @GetMapping("{stationId}/sensors/{sensorId}")
    public ResponseEntity<Sensor> getSensor(
            @PathVariable(value = "stationId") Long stationId,
            @PathVariable(value = "sensorId") String sensorId) {
        return sensorService.getSensor(stationId, sensorId);
    }

    @PreAuthorize("hasRole('STATION')")
    @PostMapping("{stationId}/sensors")
    public ResponseEntity<NewSensorResponse> addSensor(
            @PathVariable(value = "stationId") Long stationId,
            @RequestBody NewSensorRequest newSensor) {
        return sensorService.addSensor(stationId, newSensor);
    }

}
