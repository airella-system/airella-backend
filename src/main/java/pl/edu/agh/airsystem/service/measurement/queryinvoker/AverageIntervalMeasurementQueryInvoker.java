package pl.edu.agh.airsystem.service.measurement.queryinvoker;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.IntervalMeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.SingleValueMeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.TimespanResponse;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.repository.MeasurementRepository;
import pl.edu.agh.airsystem.util.Interval;

import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.AVG;
import static pl.edu.agh.airsystem.util.Intervals.generateIntervals;

@Component
@AllArgsConstructor
public class AverageIntervalMeasurementQueryInvoker implements MeasurementQueryInvoker {

    private final MeasurementRepository measurementRepository;

    @Override
    public List<? extends MeasurementResponse> apply(Sensor sensor, MeasurementQuery measurementQuery) {
        List<Interval> intervals = generateIntervals(measurementQuery.getTimespan(),
                measurementQuery.getInterval());

        List<IntervalMeasurementResponse> measurementResponses = new ArrayList<>();
        for (Interval interval : intervals) {
            TimespanResponse timespan = new TimespanResponse(
                    interval.getStart().toString(),
                    interval.getEnd().toString());
            try {
                Double avg = measurementRepository.findAverageBySensorAndTimestampAfterAndTimestampBefore(
                        sensor, interval.getStart(), interval.getEnd());
                measurementResponses.add(new IntervalMeasurementResponse(timespan, avg));
            } catch (EmptyResultDataAccessException ex) {
                measurementResponses.add(new IntervalMeasurementResponse(timespan, null));
                ex.printStackTrace();
            }
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
