package pl.edu.agh.airsystem.model.api.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticEnumDefinition;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddStatisticRequest {
    private String id;
    private String name;
    private String type;
    private String privacyMode;
    private List<StatisticEnumDefinition> listOfEnumValues;
    private String metric;
}
