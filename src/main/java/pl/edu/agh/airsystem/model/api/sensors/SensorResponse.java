package pl.edu.agh.airsystem.model.api.sensors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
public class SensorResponse {
    private List<MeasurementResponse> values = new ArrayList<>();

    public SensorResponse(Sensor sensor, MeasurementQuery measurementQuery) {
        if (measurementQuery.getStartDate() == null &&
                measurementQuery.getEndDate() == null &&
                measurementQuery.getInterval() == null) {
            createSingleValueResponse(sensor);
        } else if (measurementQuery.getStartDate() != null &&
                measurementQuery.getEndDate() != null) {
            Duration interval = measurementQuery.getInterval() != null ?
                    measurementQuery.getInterval() : Duration.ofDays(1);
            createQueriedValueResponse(sensor, measurementQuery.getStartDate(), measurementQuery.getEndDate(), interval);
        } else if (measurementQuery.getInterval() != null) {
            createQueriedValueResponse(sensor, LocalDateTime.now().minusDays(7), LocalDateTime.now(), measurementQuery.getInterval());
        }
    }

    private void createQueriedValueResponse(Sensor sensor, LocalDateTime startDate, LocalDateTime endDate, Duration interval) {
        Stream.iterate(endDate, currentEndDate -> currentEndDate.minus(interval))
                .limit(Duration.between(startDate, endDate).dividedBy(interval) + 1)
                .forEach(currentEndDate -> {
                    LocalDateTime currentStartDate = getCurrentStartDate(startDate, currentEndDate, interval);
                    sensor.getMeasurements().stream()
                            .filter(e -> e.getTimestamp().isAfter(currentStartDate))
                            .filter(e -> e.getTimestamp().isBefore(currentEndDate))
                            .max(Comparator.comparing(Measurement::getTimestamp))
                            .ifPresent(e -> values.add(new MeasurementResponse(e)));
                });
    }

    private LocalDateTime getCurrentStartDate(LocalDateTime startDate, LocalDateTime currentEndDate, Duration interval) {
        LocalDateTime currentStartDate = currentEndDate.minus(interval);
        if (currentStartDate.isBefore(startDate)) {
            currentStartDate = startDate;
        }
        return currentStartDate;
    }

    private void createSingleValueResponse(Sensor sensor) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Measurement> sensorValue = sensor.getMeasurements().stream()
                .filter(e -> e.getTimestamp().isBefore(now))
                .max(Comparator.comparing(Measurement::getTimestamp));

        sensorValue.ifPresent(measurement -> values.add(new MeasurementResponse(measurement)));
    }

}
