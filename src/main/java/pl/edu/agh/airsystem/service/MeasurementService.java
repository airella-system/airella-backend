package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.exception.NotUsersStationException;
import pl.edu.agh.airsystem.model.api.measurement.NewMeasurementRequest;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.StationClient;
import pl.edu.agh.airsystem.repository.MeasurementRepository;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class MeasurementService {
    private final ResourceFinder resourceFinder;
    private final AuthorizationService authorizationService;
    private final MeasurementRepository measurementRepository;

    public ResponseEntity<?> addMeasurement(
            Long stationId,
            String sensorId,
            NewMeasurementRequest newMeasurementRequest) {
        StationClient stationClient = authorizationService.checkAuthenticationAndGetStationClient();

        Sensor sensor = resourceFinder.findSensorInStation(stationId, sensorId);

        if (sensor.getStation().getStationClient().getId() != stationClient.getId()) {
            throw new NotUsersStationException();
        }

        Measurement measurement = new Measurement(
                sensor,
                new Timestamp(System.currentTimeMillis()),
                newMeasurementRequest.getValue());

        sensor.getMeasurements().add(measurement);
        measurementRepository.save(measurement);

        return ResponseEntity.ok().build();
    }
}

