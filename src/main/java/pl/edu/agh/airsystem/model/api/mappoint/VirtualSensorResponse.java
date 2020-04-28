package pl.edu.agh.airsystem.model.api.mappoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;

import java.util.List;

@Getter
@AllArgsConstructor
public class VirtualSensorResponse {
    private String type;
    private List<? extends MeasurementResponse> values;
    private Double status;
}
