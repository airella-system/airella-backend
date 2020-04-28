package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.mappoint.VirtualSensorResponse;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.api.sensors.SingleValueVirtualMeasurementResponse;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.service.queryinvoker.QueryInvoker;

import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.agh.airsystem.util.AirStatusUtils.calculateSensorStatus;

@Component
@AllArgsConstructor
public class VirtualSensorResponseAssembler {
    private List<QueryInvoker> queryInvokers;

    public VirtualSensorResponse assemble(Sensor sensor) {
        List<? extends MeasurementResponse> measurementResponses =
                sensor.getMeasurements().stream()
                        .map(measurement -> new SingleValueVirtualMeasurementResponse(measurement.getValue()))
                        .collect(Collectors.toList());

        return new VirtualSensorResponse(
                sensor.getType().getCode(),
                measurementResponses,
                calculateSensorStatus(sensor));
    }

}
