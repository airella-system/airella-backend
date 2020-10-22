package pl.edu.agh.airsystem.model.api.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticEnumDefinition;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticEnumDefinitionDTO {
    private String id;
    private String name;

    public StatisticEnumDefinitionDTO(StatisticEnumDefinition enumDefinition) {
        this.id = enumDefinition.getId();
        this.name = enumDefinition.getName();
    }
}
