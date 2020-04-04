package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.airsystem.exception.NewSensorIdDuplicatedException;
import pl.edu.agh.airsystem.exception.NotUsersStationException;
import pl.edu.agh.airsystem.model.api.sensors.BriefSensorResponse;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.StationClient;
import pl.edu.agh.airsystem.model.database.converter.SensorTypeConverter;
import pl.edu.agh.airsystem.repository.SensorRepository;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SensorService {
    private final ResourceFinder resourceFinder;
    private final SensorRepository sensorRepository;
    private final AuthorizationService authorizationService;

    public ResponseEntity<Map<String, BriefSensorResponse>> getSensors(Long stationId) {
        Station station = resourceFinder.findStation(stationId);

        Map<String, BriefSensorResponse> sensors = station.getSensors().stream()
                .collect(Collectors.toMap(Sensor::getId, BriefSensorResponse::new));

        return ResponseEntity.ok().body(sensors);
    }

    public ResponseEntity<?> addSensor(Long stationId, NewSensorRequest newSensor) {
        StationClient loggedStation = authorizationService.checkAuthenticationAndGetStationClient();

        Station station = resourceFinder.findStation(stationId);

        if (station.getStationClient().getId() != loggedStation.getId()) {
            throw new NotUsersStationException();
        }

        if (station.getSensors().stream()
                .anyMatch(e -> e.getId().equals(newSensor.getId()))) {
            throw new NewSensorIdDuplicatedException();
        }

        Sensor sensor = new Sensor(station,
                newSensor.getId(),
                new SensorTypeConverter().convertToEntityAttribute(newSensor.getType()));
        station.getSensors().add(sensor);

        sensorRepository.save(sensor);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensor.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .build();
    }

    public ResponseEntity<BriefSensorResponse> getSensor(Long stationId, String sensorId) {
        Sensor sensor = resourceFinder.findSensorInStation(stationId, sensorId);
        BriefSensorResponse response = new BriefSensorResponse(sensor);

        return ResponseEntity.ok().body(response);
    }

}
