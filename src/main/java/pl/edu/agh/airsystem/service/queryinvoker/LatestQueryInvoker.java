package pl.edu.agh.airsystem.service.queryinvoker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.SingleValueMeasurementResponse;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.util.SensorUtilsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.LATEST;

@Component
@AllArgsConstructor
public class LatestQueryInvoker implements QueryInvoker {
    private SensorUtilsService sensorUtilsService;

    @Override
    public List<? extends MeasurementResponse> apply(Sensor sensor, MeasurementQuery measurementQuery) {
        List<MeasurementResponse> measurementResponses = new ArrayList<>();
        Optional<Measurement> sensorValue = sensorUtilsService.findLatestMeasurementInSensor(sensor);

        sensorValue.ifPresent(measurement -> measurementResponses.add(new SingleValueMeasurementResponse(measurement)));

        return measurementResponses;
    }

    @Override
    public boolean isApplicable(MeasurementQuery measurementQuery) {
        return measurementQuery.getStrategy() == LATEST &&
                measurementQuery.getTimespan() == null &&
                measurementQuery.getInterval() == null;
    }

}
