package pl.edu.agh.airsystem.model.api.sensors;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class IntervalTimeMeasurementResponse extends IntervalMeasurementResponse {
    private Instant timestamp;

    public IntervalTimeMeasurementResponse(TimespanResponse timespanResponse,
                                           Instant timestamp, Double value) {
        super(timespanResponse, value);
        this.timestamp = timestamp;
    }

}
