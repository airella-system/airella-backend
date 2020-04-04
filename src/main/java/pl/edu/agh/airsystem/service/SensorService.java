package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.exception.NewSensorIdDuplicatedException;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.converter.SensorTypeConverter;
import pl.edu.agh.airsystem.model.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.model.sensors.NewSensorResponse;
import pl.edu.agh.airsystem.model.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.stations.BriefSensorResponse;
import pl.edu.agh.airsystem.repository.SensorRepository;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SensorService {
    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;

    public ResponseEntity<List<BriefSensorResponse>> getSensors(Long stationId) {
        List<BriefSensorResponse> body = stationRepository.findById(stationId).get()
                .getSensors().stream()
                .map(BriefSensorResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(body);
    }

    public ResponseEntity<NewSensorResponse> addSensor(Long stationId, NewSensorRequest newSensor) {
        Station station = stationRepository.findById(stationId).get();

        if (station.getSensors().stream()
                .anyMatch(e -> e.getId().equals(newSensor.getId()))) {
            throw new NewSensorIdDuplicatedException();
        }

        Sensor sensor = new Sensor(station,
                newSensor.getId(),
                new SensorTypeConverter().convertToEntityAttribute(newSensor.getType()));
        station.getSensors().add(sensor);

        sensorRepository.save(sensor);

        return ResponseEntity.ok()
                .body(new NewSensorResponse(station.getSensors().indexOf(sensor)));
    }

    public ResponseEntity<Sensor> getSensor(Long stationId, String sensorId) {
        SensorResponse body = new SensorResponse(stationRepository.findById(stationId).get().getSensors()
                .stream().filter(e -> e.getId().equals(sensorId)).findFirst().get());
        return ResponseEntity.ok().body(body);
    }

}
