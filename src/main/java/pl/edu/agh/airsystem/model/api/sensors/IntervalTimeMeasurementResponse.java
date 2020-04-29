package pl.edu.agh.airsystem.model.api.sensors;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IntervalTimeMeasurementResponse extends IntervalMeasurementResponse {
    private LocalDateTime timestamp;

    public IntervalTimeMeasurementResponse(TimespanResponse timespanResponse,
                                           LocalDateTime timestamp, Double value) {
        super(timespanResponse, value);
        this.timestamp = timestamp;
    }

}
