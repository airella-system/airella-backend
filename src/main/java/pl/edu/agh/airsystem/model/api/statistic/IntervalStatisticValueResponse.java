package pl.edu.agh.airsystem.model.api.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.sensors.TimespanResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntervalStatisticValueResponse extends StatisticValueResponse {
    private TimespanResponse timespan;
    private Object value;
}
