package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.airsystem.assembler.SensorResponseAssembler;
import pl.edu.agh.airsystem.converter.SensorTypeConverter;
import pl.edu.agh.airsystem.exception.NewSensorIdDuplicatedException;
import pl.edu.agh.airsystem.exception.NotUsersStationException;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorResponse;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.StationClient;
import pl.edu.agh.airsystem.repository.SensorRepository;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class SensorService {
    private final ResourceFinder resourceFinder;
    private final SensorRepository sensorRepository;
    private final AuthorizationService authorizationService;
    private final SensorResponseAssembler sensorResponseAssembler;

    public ResponseEntity<DataResponse> getSensors(String stationId, MeasurementQuery measurementQuery) {
        Map<String, SensorResponse> sensors = sensorRepository.findByStation_IdAndTypeIn(
                stationId, measurementQuery.getTypes()).stream()
                .collect(Collectors.toMap(Sensor::getId,
                        sensor -> sensorResponseAssembler.assemble(sensor, measurementQuery)));

        return ResponseEntity.ok().body(DataResponse.of(sensors));
    }

    public static boolean filterSensorType(Sensor sensor, List<SensorType> types) {
        if (types == null) return true;
        return types.contains(sensor.getType());
    }

    public void createSensor(String stationId, NewSensorRequest newSensor) {

    }

    public ResponseEntity<Response> addSensor(String stationId, NewSensorRequest newSensor) {
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
                newSensor.getName(),
                SensorTypeConverter.convertStringToEnum(newSensor.getType()));
        station.getSensors().add(sensor);

        sensorRepository.save(sensor);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensor.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(DataResponse.of(new NewSensorResponse(sensor.getId())));
    }


    public void addSensors(String stationId, List<NewSensorRequest> newSensorRequestsList) {
        newSensorRequestsList.forEach(newSensorRequest -> addSensor(stationId, newSensorRequest));
    }

    public ResponseEntity<DataResponse> getSensor(String stationId, String sensorId, MeasurementQuery measurementQuery) {
        Sensor sensor = resourceFinder.findSensorInStation(stationId, sensorId);
        SensorResponse response = sensorResponseAssembler.assemble(sensor, measurementQuery);

        return ResponseEntity.ok().body(DataResponse.of(response));
    }

}
