package pl.edu.agh.airsystem.model.api.sensors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntervalMeasurementResponse extends MeasurementResponse {
    private TimespanResponse timespan;
    private Double value;
}
