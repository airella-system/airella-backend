package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;
import pl.edu.agh.airsystem.model.database.statistic.StatisticType;

@Getter
@AllArgsConstructor
public class GeneratorStatisticDefinition {
    private String id;
    private String name;
    private StatisticType type;
    private StatisticPrivacyMode privacyMode;
    private GeneratorStatisticValueDefinition generatorStatisticValueDefinition;
}
