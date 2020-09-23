package pl.edu.agh.airsystem.model.api.statistic;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticEnumDefinition;
import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;
import pl.edu.agh.airsystem.model.database.statistic.StatisticType;

import java.util.List;

@Getter
@Setter
public class MultipleEnumsStatisticResponse extends StatisticResponse {
    private List<StatisticEnumDefinition> enumDefinitions;

    public MultipleEnumsStatisticResponse(String id, String name, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode, List<StatisticEnumDefinition> enumDefinitions, List<? extends StatisticValueResponse> values) {
        super(id, name, statisticType, statisticPrivacyMode, values);
        this.enumDefinitions = enumDefinitions;
    }
}
