package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.exception.NotUsersStationException;
import pl.edu.agh.airsystem.model.api.measurement.NewMeasurementRequest;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.response.SuccessResponse;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.StationClient;
import pl.edu.agh.airsystem.util.MeasurementUtilsService;

import java.time.Instant;

@Service
@Transactional
@AllArgsConstructor
public class MeasurementService {
    private final ResourceFinder resourceFinder;
    private final AuthorizationService authorizationService;
    private final MeasurementUtilsService measurementUtilsService;

    public ResponseEntity<Response> addMeasurement(
            String stationId,
            String sensorId,
            NewMeasurementRequest newMeasurementRequest) {
        StationClient stationClient = authorizationService.checkAuthenticationAndGetStationClient();

        Sensor sensor = resourceFinder.findSensorInStation(stationId, sensorId);

        if (sensor.getStation().getStationClient().getId() != stationClient.getId()) {
            throw new NotUsersStationException();
        }

        Instant date;
        if (newMeasurementRequest.getDate() != null) {
            date = Instant.parse(newMeasurementRequest.getDate());
        } else {
            date = Instant.now();
        }

        Measurement measurement = new Measurement(
                sensor,
                date,
                newMeasurementRequest.getValue());
        measurementUtilsService.addNewMeasurement(sensor, measurement);


        return ResponseEntity.ok(new SuccessResponse());
    }
}

