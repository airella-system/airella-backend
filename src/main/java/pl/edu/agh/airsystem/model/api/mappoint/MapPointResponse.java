package pl.edu.agh.airsystem.model.api.mappoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MapPointResponse {
    private final List<VirtualSensorResponse> virtualSensors;
    private Double aqi;
}
