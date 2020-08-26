package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.Station;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AirStatusService {
    private SensorUtilsService sensorUtilsService;

    // From https://www.eea.europa.eu/themes/air/air-quality-concentrations/air-quality-standards
    private static final Map<SensorType, Double> normsForSensorTypesMap = Map.of(
            SensorType.PM2_5, 25.0,
            SensorType.PM10, 50.0
    );

    // From https://en.wikipedia.org/wiki/Air_quality_index#Europe
    private static final List<Interval> caqiForPm10 = List.of(
            new Interval(0, 25, 0, 25),
            new Interval(25, 50, 25, 50),
            new Interval(50, 90, 50, 75),
            new Interval(90, 180, 75, 100)
    );

    private static final List<Interval> caqiForPm2_5 = List.of(
            new Interval(0, 15, 0, 25),
            new Interval(15, 30, 25, 50),
            new Interval(30, 55, 50, 75),
            new Interval(55, 110, 75, 100)
    );

    private static final Map<SensorType, List<Interval>> caqiForSensorTypesMap = Map.of(
            SensorType.PM10, caqiForPm10,
            SensorType.PM2_5, caqiForPm2_5
    );

    public Double calculateSensorStatus(Sensor sensor) {
        if (!normsForSensorTypesMap.containsKey(sensor.getType())) {
            return null;
        }
        return sensorUtilsService.findLatestMeasurementInSensor(sensor)
                .map(Measurement::getValue)
                .map(value -> value / normsForSensorTypesMap.get(sensor.getType()))
                .orElse(null);
    }

    public Double calculateAirQualityIndex(Station station) {
        return caqiForSensorTypesMap.keySet().stream()
                .map(sensorType -> getCaqiForSensor(station, sensorType))
                .filter(Objects::nonNull)
                .max(Double::compareTo)
                .orElse(null);
    }

    private Double getCaqiForSensor(Station station, SensorType sensorType) {
        return station.getSensors().stream()
                .filter(e -> e.getType() == sensorType)
                .findFirst()
                .flatMap(sensor -> sensorUtilsService.findLatestMeasurementInSensor(sensor)
                        .map(Measurement::getValue))
                .map(value -> calculateValueFromIntervals(caqiForSensorTypesMap.get(sensorType), value))
                .orElse(null);
    }

    public double calculateValueFromIntervals(List<Interval> intervals, double input) {
        return intervals.stream()
                .filter(interval -> interval.isInInterval(input))
                .findFirst()
                .map(interval -> interval.map(input))
                .orElse(intervals.get(intervals.size() - 1).map(input));
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Interval {
        private final double inputFrom;
        private final double inputTo;
        private final double outputFrom;
        private final double outputTo;

        public boolean isInInterval(double input) {
            return input >= inputFrom && input <= inputTo;
        }

        public double map(double input) {
            double percentage = (input - inputFrom) / (inputTo - inputFrom);
            return outputFrom + (outputTo - outputFrom) * percentage;
        }
    }

}
