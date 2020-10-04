package pl.edu.agh.airsystem.model.api.sensors;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class IntervalTimeMeasurementResponse extends IntervalMeasurementResponse {
    private String timestamp;

    public IntervalTimeMeasurementResponse(TimespanResponse timespanResponse,
                                           String timestamp, Double value) {
        super(timespanResponse, value);
        this.timestamp = timestamp;
    }

}
