package pl.edu.agh.airsystem.model.api.mappoint;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VirtualStationResponse {
    private final List<VirtualSensorResponse> sensors;
    private Double caqi;
}
