package pl.edu.agh.airsystem.model.api.sensors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntervalMeasurementResponse extends MeasurementResponse {
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private Double value;
}
