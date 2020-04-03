package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.converter.SensorTypeConverter;
import pl.edu.agh.airsystem.model.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.model.sensors.NewSensorResponse;
import pl.edu.agh.airsystem.model.stations.BriefSensorResponse;
import pl.edu.agh.airsystem.repository.SensorRepository;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stations")
@AllArgsConstructor
public class SensorController {
    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;

    @GetMapping("{stationId}/sensors")
    @ResponseBody
    public List<BriefSensorResponse> getSensors(@PathVariable(value = "stationId") Long stationId) {
        return stationRepository.findById(stationId).get()
                .getSensors().stream()
                .map(BriefSensorResponse::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('STATION')")
    @PostMapping("{stationId}/sensors")
    @ResponseBody
    public NewSensorResponse addSensor(@PathVariable(value = "stationId") Long stationId,
                                       @RequestBody NewSensorRequest newSensor) {
        Station station = stationRepository.findById(stationId).get();

        Sensor sensor = new Sensor(station, new SensorTypeConverter().convertToEntityAttribute(newSensor.getSensorType()));
        station.getSensors().add(sensor);

        sensorRepository.save(sensor);

        return new NewSensorResponse(sensor);
    }

    @GetMapping("{stationId}/sensors/{sensorId}")
    @ResponseBody
    public Sensor getSensor(@PathVariable(value = "stationId") Long stationId,
                            @PathVariable(value = "sensorId") Long sensorId) {
        return stationRepository.findById(stationId).get().getSensors().get(Math.toIntExact(sensorId));
    }
}
