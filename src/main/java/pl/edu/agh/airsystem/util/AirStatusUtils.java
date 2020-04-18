package pl.edu.agh.airsystem.util;

import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.Station;

import java.util.Comparator;

public class AirStatusUtils {

    //TODO add calculation method
    public static Double calculateSensorStatus(Sensor sensor) {
        Double sensorLastValue = getSensorLastValue(sensor);
        if (sensorLastValue == null) {
            return null;
        } else {
            return 50.0;
        }
    }

    //TODO add calculation method
    public static Double calculateAirQualityIndex(Station station) {
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
