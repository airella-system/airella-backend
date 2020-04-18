package pl.edu.agh.airsystem.assembler;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class SensorResponseAssembler {

    public SensorResponse assemble(Sensor sensor, MeasurementQuery measurementQuery) {
        if (measurementQuery.getStartDate() == null &&
                measurementQuery.getEndDate() == null &&
                measurementQuery.getInterval() == null) {
            return createSingleValueResponse(sensor);
        } else if (measurementQuery.getStartDate() != null &&
                measurementQuery.getEndDate() != null) {
            Duration interval = measurementQuery.getInterval() != null ?
                    measurementQuery.getInterval() : Duration.ofDays(1);
            return createQueriedValueResponse(sensor, measurementQuery.getStartDate(),
                    measurementQuery.getEndDate(), interval);
        } else if (measurementQuery.getInterval() != null) {
            return createQueriedValueResponse(sensor, LocalDateTime.now().minusDays(7),
                    LocalDateTime.now(), measurementQuery.getInterval());
        }
        return null;
    }

    private SensorResponse createQueriedValueResponse(Sensor sensor, LocalDateTime startDate,
                                                      LocalDateTime endDate, Duration interval) {
        List<MeasurementResponse> measurementResponses = new ArrayList<>();

        Stream.iterate(endDate, currentEndDate -> currentEndDate.minus(interval))
                .limit(Duration.between(startDate, endDate).dividedBy(interval) + 1)
                .forEach(currentEndDate -> {
                    LocalDateTime currentStartDate = getCurrentStartDate(startDate, currentEndDate, interval);
                    sensor.getMeasurements().stream()
                            .filter(e -> e.getTimestamp().isAfter(currentStartDate))
                            .filter(e -> e.getTimestamp().isBefore(currentEndDate))
                            .max(Comparator.comparing(Measurement::getTimestamp))
                            .ifPresent(e -> measurementResponses.add(new MeasurementResponse(e)));
                });
        return new SensorResponse(sensor.getId(), sensor.getType().getCode(),
                measurementResponses, calculateSensorStatus(sensor));
    }

    private LocalDateTime getCurrentStartDate(LocalDateTime startDate,
                                              LocalDateTime currentEndDate, Duration interval) {
        LocalDateTime currentStartDate = currentEndDate.minus(interval);
        if (currentStartDate.isBefore(startDate)) {
            currentStartDate = startDate;
        }
        return currentStartDate;
    }

    private SensorResponse createSingleValueResponse(Sensor sensor) {
        List<MeasurementResponse> measurementResponses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Optional<Measurement> sensorValue = sensor.getMeasurements().stream()
                .filter(e -> e.getTimestamp().isBefore(now))
                .max(Comparator.comparing(Measurement::getTimestamp));

        sensorValue.ifPresent(measurement -> measurementResponses.add(new MeasurementResponse(measurement)));
        return new SensorResponse(sensor.getId(), sensor.getType().getCode(),
                measurementResponses, calculateSensorStatus(sensor));
    }

    private Double calculateSensorStatus(Sensor sensor) {
        Measurement measurement = getLatestMeasurement(sensor);
        if (measurement == null) {
            return null;
        } else {
            //TODO add calculation method
            return 50.0;
        }
    }

    private Measurement getLatestMeasurement(Sensor sensor) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Measurement> sensorValue = sensor.getMeasurements().stream()
                .filter(e -> e.getTimestamp().isBefore(now))
                .max(Comparator.comparing(Measurement::getTimestamp));

        return sensorValue.orElse(null);
    }
}
