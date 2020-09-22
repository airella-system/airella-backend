package pl.edu.agh.airsystem.model.api.statistic;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;
import pl.edu.agh.airsystem.model.database.statistic.StatisticType;

import java.util.List;

@Getter
@Setter
public class MultipleFloatsStatisticResponse extends StatisticResponse {
    private String metric;

    public MultipleFloatsStatisticResponse(String id, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode, String metric, List<? extends StatisticValueResponse> values) {
        super(id, statisticType, statisticPrivacyMode, values);
        this.metric = metric;
    }
}
