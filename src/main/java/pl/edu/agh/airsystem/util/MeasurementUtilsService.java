package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.repository.MeasurementRepository;
import pl.edu.agh.airsystem.repository.SensorRepository;

@Service
@Transactional
@AllArgsConstructor
public class MeasurementUtilsService {
    private MeasurementRepository measurementRepository;
    private SensorRepository sensorRepository;

    public void addNewMeasurement(Sensor sensor, Measurement measurement) {
        sensor.setLatestMeasurement(measurement);
        sensor.getMeasurements().add(measurement);
        measurementRepository.save(measurement);
        sensorRepository.save(sensor);
    }

    public void addNewMeasurement(long sensorDbId, Measurement measurement) {
        Sensor sensor = sensorRepository.findById(sensorDbId).get();
        sensor.setLatestMeasurement(measurement);
        sensor.getMeasurements().add(measurement);
        measurementRepository.save(measurement);
        sensorRepository.save(sensor);
    }

}
