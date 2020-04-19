package pl.edu.agh.airsystem.model.api.sensors;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IntervalTimeMeasurementResponse extends IntervalMeasurementResponse {
    private LocalDateTime timestamp;

    public IntervalTimeMeasurementResponse(LocalDateTime start, LocalDateTime end,
                                           LocalDateTime timestamp, Double value) {
        super(start, end, value);
        this.timestamp = timestamp;
    }

}
