package pl.edu.agh.airsystem.service.queryinvoker;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.SingleValueMeasurementResponse;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.ALL;

@Component
public class MultipleQueryInvoker implements QueryInvoker {

    @Override
    public List<? extends MeasurementResponse> apply(Sensor sensor, MeasurementQuery measurementQuery) {
        LocalDateTime startDate = measurementQuery.getTimespan().getStart();
        LocalDateTime endDate = measurementQuery.getTimespan().getEnd();

        List<MeasurementResponse> measurementResponses = new ArrayList<>();
        sensor.getMeasurements().stream()
                .filter(e -> e.getTimestamp().isAfter(startDate))
                .filter(e -> e.getTimestamp().isBefore(endDate))
                .forEach(measurement -> measurementResponses.add(new SingleValueMeasurementResponse(measurement)));

        return measurementResponses;
    }

    @Override
    public boolean isApplicable(MeasurementQuery measurementQuery) {
        return measurementQuery.getStrategy() == ALL &&
                measurementQuery.getTimespan() != null &&
                measurementQuery.getInterval() == null;
    }

}
