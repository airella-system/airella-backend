package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.converter.MeasurementQueryConverter;
import pl.edu.agh.airsystem.model.api.DataResponse;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryRequest;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.service.SensorService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class SensorController {
    private final SensorService sensorService;
    private final MeasurementQueryConverter measurementQueryConverter;

    @GetMapping("{stationId}/sensors")
    public ResponseEntity<DataResponse> getSensors(
            @PathVariable(value = "stationId") Long stationId,
            MeasurementQueryRequest measurementQueryRequest) {
        return sensorService.getSensors(stationId,
                measurementQueryConverter.convert(measurementQueryRequest));
    }

    @GetMapping("{stationId}/sensors/{sensorId}")
    public ResponseEntity<DataResponse> getSensor(
            @PathVariable(value = "stationId") Long stationId,
            @PathVariable(value = "sensorId") String sensorId,
            MeasurementQueryRequest measurementQueryRequest) {
        return sensorService.getSensor(stationId, sensorId,
                measurementQueryConverter.convert(measurementQueryRequest));
    }

    @PostMapping("{stationId}/sensors")
    public ResponseEntity<DataResponse> addSensor(
            @PathVariable(value = "stationId") Long stationId,
            @RequestBody NewSensorRequest newSensor) {
        return sensorService.addSensor(stationId, newSensor);
    }


}
