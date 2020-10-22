package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.statistic.StatisticEnumDefinitionDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticEnumDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dbId;

    private String id;
    private String name;

    public StatisticEnumDefinition(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public StatisticEnumDefinition(StatisticEnumDefinitionDTO enumDefinitionDTO) {
        this.id = enumDefinitionDTO.getId();
        this.name = enumDefinitionDTO.getName();
    }
}
