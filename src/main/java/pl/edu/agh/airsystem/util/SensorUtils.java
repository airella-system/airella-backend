package pl.edu.agh.airsystem.util;

import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

public class SensorUtils {

    public static Optional<Measurement> getLatestMeasurement(Sensor sensor) {
        LocalDateTime now = LocalDateTime.now();
        return sensor.getMeasurements().stream()
                .filter(e -> e.getTimestamp().isBefore(now))
                .max(Comparator.comparing(Measurement::getTimestamp));
    }

}
