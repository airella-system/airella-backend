package pl.edu.agh.airsystem.util;

import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.Station;

import java.util.Comparator;

public class AirStatusUtils {
    public static Double calculateAirStatus(Station station) {
        return station.getSensors().stream()
                .filter(e -> e.getType() == SensorType.PM10)
                .findFirst()
                .map(AirStatusUtils::getSensorLastValue)
                .orElse(null);
    }

    private static Double getSensorLastValue(Sensor sensor) {
        return sensor.getMeasurements().stream()
                .max(Comparator.comparing(Measurement::getTimestamp))
                .map(Measurement::getValue)
                .orElse(null);
    }
}
