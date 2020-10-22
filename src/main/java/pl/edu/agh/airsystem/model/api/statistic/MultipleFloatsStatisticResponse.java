package pl.edu.agh.airsystem.model.api.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultipleFloatsStatisticResponse extends StatisticResponse {
    private String metric;
    private String chartType;

    public MultipleFloatsStatisticResponse(String id, String name, String statisticType, String statisticPrivacyMode, String metric, List<? extends StatisticValueResponse> values, String chartType) {
        super(id, name, statisticType, statisticPrivacyMode, values);
        this.metric = metric;
        this.chartType = chartType;
    }
}
