package pl.edu.agh.airsystem.model.api.sensors;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SensorResponse {
    private final String id;
    private final String type;
    private final List<? extends MeasurementResponse> values;
    private final Double status;
}
