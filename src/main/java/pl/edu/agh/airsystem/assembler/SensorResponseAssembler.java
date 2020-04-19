package pl.edu.agh.airsystem.assembler;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy;
import pl.edu.agh.airsystem.model.api.sensors.*;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.util.Interval;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.*;
import static pl.edu.agh.airsystem.util.AirStatusUtils.calculateSensorStatus;

@Component
public class SensorResponseAssembler {

    public SensorResponse assemble(Sensor sensor, MeasurementQuery measurementQuery) {
        if (measurementQuery.getStartDate() == null &&
                measurementQuery.getEndDate() == null &&
                measurementQuery.getInterval() == null) {
            return createSingleValueResponse(sensor);
        } else if (measurementQuery.getStartDate() != null &&
                measurementQuery.getEndDate() != null &&
                measurementQuery.getInterval() != null) {
            Duration interval = measurementQuery.getInterval();
            return createQueriedValueResponse(sensor, measurementQuery.getStartDate(),
                    measurementQuery.getEndDate(), interval,
                    measurementQuery.getStrategy(), measurementQuery.isInterpolate());
        } else if (measurementQuery.getStartDate() != null &&
                measurementQuery.getEndDate() != null &&
                measurementQuery.getInterval() == null && measurementQuery.getStrategy() == ALL) {
            return createMultipleValueResponse(sensor, measurementQuery.getStartDate(),
                    measurementQuery.getEndDate());
        } else if (measurementQuery.getInterval() != null) {
            return createQueriedValueResponse(sensor, LocalDateTime.now().minusDays(7),
                    LocalDateTime.now(), measurementQuery.getInterval(),
                    measurementQuery.getStrategy(), measurementQuery.isInterpolate());
        }
        return null;
    }

    private SensorResponse createQueriedValueResponse(Sensor sensor, LocalDateTime startDate,
                                                      LocalDateTime endDate, Duration intervalDuration,
                                                      MeasurementQueryStrategy strategy,
                                                      boolean interpolate) {
        List<IntervalMeasurementResponse> measurementResponses = new ArrayList<>();

        List<Interval> intervals = Stream.iterate(endDate, currentEndDate -> currentEndDate.minus(intervalDuration))
                .limit(Duration.between(startDate, endDate).dividedBy(intervalDuration) + 1)
                .map(currentEndDate -> new Interval(getCurrentStartDate(startDate, currentEndDate, intervalDuration), currentEndDate))
                .collect(Collectors.toList());

        if (strategy == LATEST) {
            for (Interval interval : intervals) {
                sensor.getMeasurements().stream()
                        .filter(e -> e.getTimestamp().isAfter(interval.getStart()))
                        .filter(e -> e.getTimestamp().isBefore(interval.getEnd()))
                        .max(Comparator.comparing(Measurement::getTimestamp))
                        .ifPresentOrElse(e -> measurementResponses.add(
                                new IntervalTimeMeasurementResponse(interval.getStart(),
                                        interval.getEnd(), e.getTimestamp(), e.getValue())),
                                () -> measurementResponses.add(
                                        new IntervalTimeMeasurementResponse(interval.getStart(),
                                                interval.getEnd(), null, null)));
            }
        } else if (strategy == AVG) {
            for (Interval interval : intervals) {
                sensor.getMeasurements().stream()
                        .filter(e -> e.getTimestamp().isAfter(interval.getStart()))
                        .filter(e -> e.getTimestamp().isBefore(interval.getEnd()))
                        .mapToDouble(Measurement::getValue)
                        .average()
                        .ifPresentOrElse(e -> measurementResponses.add(
                                new IntervalMeasurementResponse(interval.getStart(),
                                        interval.getEnd(), e)),
                                () -> measurementResponses.add(
                                        new IntervalMeasurementResponse(interval.getStart(),
                                                interval.getEnd(), null)));
            }
        }

        if (interpolate) {
            for (int i = 2; i < measurementResponses.size() - 1; i++) {
                IntervalMeasurementResponse prevMeasurement = measurementResponses.get(i - 1);
                IntervalMeasurementResponse currMeasurement = measurementResponses.get(i);
                IntervalMeasurementResponse nextMeasurement = measurementResponses.get(i + 1);

                if (prevMeasurement.getValue() != null &&
                        currMeasurement.getValue() == null &&
                        nextMeasurement.getValue() != null) {
                    currMeasurement.setValue(
                            (prevMeasurement.getValue() + nextMeasurement.getValue()) / 2.0);
                }
            }
        }


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

    private SensorResponse createMultipleValueResponse(Sensor sensor, LocalDateTime startDate, LocalDateTime endDate) {
        List<MeasurementResponse> measurementResponses = new ArrayList<>();
        sensor.getMeasurements().stream()
                .filter(e -> e.getTimestamp().isAfter(startDate))
                .filter(e -> e.getTimestamp().isBefore(endDate))
                .forEach(measurement -> measurementResponses.add(new SingleValueMeasurementResponse(measurement)));

        return new SensorResponse(sensor.getId(), sensor.getType().getCode(),
                measurementResponses, calculateSensorStatus(sensor));
    }

    private SensorResponse createSingleValueResponse(Sensor sensor) {
        List<MeasurementResponse> measurementResponses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Optional<Measurement> sensorValue = sensor.getMeasurements().stream()
                .filter(e -> e.getTimestamp().isBefore(now))
                .max(Comparator.comparing(Measurement::getTimestamp));

        sensorValue.ifPresent(measurement -> measurementResponses.add(new SingleValueMeasurementResponse(measurement)));
        return new SensorResponse(sensor.getId(), sensor.getType().getCode(),
                measurementResponses, calculateSensorStatus(sensor));
    }

}
