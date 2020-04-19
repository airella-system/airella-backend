package pl.edu.agh.airsystem.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementQueryRequest {
    String timespan;
    String interval;
    String sensors;
    String strategy;
    String interpolate;
}
