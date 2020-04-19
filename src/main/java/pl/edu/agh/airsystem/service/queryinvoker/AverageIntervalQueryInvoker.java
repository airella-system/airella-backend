package pl.edu.agh.airsystem.service.queryinvoker;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.IntervalMeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.util.Interval;

import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.AVG;
import static pl.edu.agh.airsystem.util.Intervals.generateIntervals;

@Component
public class AverageIntervalQueryInvoker implements QueryInvoker {

    @Override
    public List<? extends MeasurementResponse> apply(Sensor sensor, MeasurementQuery measurementQuery) {
        List<Interval> intervals = generateIntervals(measurementQuery.getTimespan(),
                measurementQuery.getInterval());

        List<IntervalMeasurementResponse> measurementResponses = new ArrayList<>();
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

        return measurementResponses;
    }

    @Override
    public boolean isApplicable(MeasurementQuery measurementQuery) {
        return measurementQuery.getStrategy() == AVG &&
                measurementQuery.getTimespan() != null &&
                measurementQuery.getInterval() != null;
    }

}
