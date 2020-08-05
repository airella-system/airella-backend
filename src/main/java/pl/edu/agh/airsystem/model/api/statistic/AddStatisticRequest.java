package pl.edu.agh.airsystem.model.api.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;
import pl.edu.agh.airsystem.model.database.statistic.StatisticType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddStatisticRequest {
    private String id;
    private StatisticType type;
    private StatisticPrivacyMode statisticPrivacyMode;
}