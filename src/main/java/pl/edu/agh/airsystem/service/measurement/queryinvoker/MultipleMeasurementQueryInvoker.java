package pl.edu.agh.airsystem.service.measurement.queryinvoker;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.SingleValueMeasurementResponse;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.repository.MeasurementRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.ALL;

@Component
public class MultipleMeasurementQueryInvoker implements MeasurementQueryInvoker {

    private MeasurementRepository measurementRepository;

    @Override
    public List<? extends MeasurementResponse> apply(Sensor sensor, MeasurementQuery measurementQuery) {
        Instant startDate = measurementQuery.getTimespan().getStart();
        Instant endDate = measurementQuery.getTimespan().getEnd();

        List<MeasurementResponse> measurementResponses = new ArrayList<>();
        measurementRepository.findBySensorAndTimestampAfterAndTimestampBefore(sensor, startDate, endDate)
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
