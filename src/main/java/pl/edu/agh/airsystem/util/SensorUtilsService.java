package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SensorUtilsService {

    public Optional<Measurement> findLatestMeasurementInSensor(Sensor sensor) {
        return Optional.ofNullable(sensor.getLatestMeasurement());
    }

}
