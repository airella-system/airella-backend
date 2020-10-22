package pl.edu.agh.airsystem.model.api.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultipleEnumsStatisticResponse extends StatisticResponse {
    private List<StatisticEnumDefinitionDTO> enumDefinitions;
    private String chartType;

    public MultipleEnumsStatisticResponse(String id, String name, String statisticType, String statisticPrivacyMode, List<StatisticEnumDefinitionDTO> enumDefinitions, List<? extends StatisticValueResponse> values, String chartType) {
        super(id, name, statisticType, statisticPrivacyMode, values);
        this.enumDefinitions = enumDefinitions;
        this.chartType = chartType;
    }
}
