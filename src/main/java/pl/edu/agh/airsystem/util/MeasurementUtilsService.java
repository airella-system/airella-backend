package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.repository.MeasurementRepository;
import pl.edu.agh.airsystem.repository.SensorRepository;

import java.time.Instant;

@Service
@Transactional
@AllArgsConstructor
public class MeasurementUtilsService {
    private MeasurementRepository measurementRepository;
    private SensorRepository sensorRepository;

    public void addNewMeasurement(Sensor sensor, Measurement measurement) {
        Measurement lastMeasurement = sensor.getLatestMeasurement();
        if (lastMeasurement == null) {
            sensor.setLatestMeasurement(measurement);
        } else if (measurement.getTimestamp().isAfter(lastMeasurement.getTimestamp()) &&
                measurement.getTimestamp().isBefore(Instant.now())) {
            sensor.setLatestMeasurement(measurement);
        }

        sensor.getMeasurements().add(measurement);
        measurementRepository.save(measurement);
        sensorRepository.save(sensor);
    }

    public void addNewMeasurement(long sensorDbId, Measurement measurement) {
        Sensor sensor = sensorRepository.findById(sensorDbId).get();

        Measurement lastMeasurement = sensor.getLatestMeasurement();
        if (lastMeasurement == null) {
            sensor.setLatestMeasurement(measurement);
        } else if (measurement.getTimestamp().isAfter(lastMeasurement.getTimestamp()) &&
                measurement.getTimestamp().isBefore(Instant.now())) {
            sensor.setLatestMeasurement(measurement);
        }

        measurement.setSensor(sensor);
        sensor.setLatestMeasurement(measurement);
        sensor.getMeasurements().add(measurement);
        measurementRepository.save(measurement);
        sensorRepository.save(sensor);
    }

}
