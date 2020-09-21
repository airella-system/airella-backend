package pl.edu.agh.airsystem.model.api.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;
import pl.edu.agh.airsystem.model.database.statistic.StatisticType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatisticResponse {
    private String id;
    private StatisticType statisticType;
    private StatisticPrivacyMode statisticPrivacyMode;
    private final List<? extends StatisticValueResponse> values;
}
