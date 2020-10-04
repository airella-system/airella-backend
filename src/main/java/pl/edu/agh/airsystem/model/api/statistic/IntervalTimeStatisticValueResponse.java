package pl.edu.agh.airsystem.model.api.statistic;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.sensors.TimespanResponse;

import java.time.Instant;

@Getter
@Setter
public class IntervalTimeStatisticValueResponse extends IntervalStatisticValueResponse {
    private String timestamp;

    public IntervalTimeStatisticValueResponse(TimespanResponse timespanResponse,
                                              String timestamp, Object value) {
        super(timespanResponse, value);
        this.timestamp = timestamp;
    }

}
