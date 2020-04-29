package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.SensorResponse;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.service.queryinvoker.QueryInvoker;
import pl.edu.agh.airsystem.util.AirStatusService;

import java.util.List;

@Component
@AllArgsConstructor
public class SensorResponseAssembler {
    private List<QueryInvoker> queryInvokers;
    private AirStatusService airStatusService;

    public SensorResponse assemble(Sensor sensor, MeasurementQuery measurementQuery) {
        List<? extends MeasurementResponse> measurementResponses = queryInvokers.stream()
                .filter(invoker -> invoker.isApplicable(measurementQuery))
                .findFirst()
                .map(invoker -> invoker.apply(sensor, measurementQuery))
                .orElse(null);

        return new SensorResponse(sensor.getId(),
                sensor.getType().getCode(),
                measurementResponses,
                airStatusService.calculateSensorStatus(sensor));
    }

}
