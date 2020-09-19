package pl.edu.agh.airsystem.model.api.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SingleStatisticValueResponse extends StatisticValueResponse {
    private Instant timestamp;
    private Object value;

    public SingleStatisticValueResponse(StatisticValue statisticValue) {
        if (statisticValue != null) {
            this.timestamp = statisticValue.getTimestamp();
            this.value = statisticValue.getValue();
        }
    }

}
