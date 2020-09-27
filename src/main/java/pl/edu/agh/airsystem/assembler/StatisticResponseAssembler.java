package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.statistic.MultipleEnumsStatisticResponse;
import pl.edu.agh.airsystem.model.api.statistic.MultipleFloatsStatisticResponse;
import pl.edu.agh.airsystem.model.api.statistic.StatisticEnumDefinitionDTO;
import pl.edu.agh.airsystem.model.api.statistic.StatisticResponse;
import pl.edu.agh.airsystem.model.api.statistic.StatisticValueResponse;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueEnumStatistic;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueFloatStatistic;
import pl.edu.agh.airsystem.model.database.statistic.OneValueStringStatistic;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.service.statistic.queryinvoker.StatisticValueQueryInvoker;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class StatisticResponseAssembler {
    private List<StatisticValueQueryInvoker> statisticValueQueryInvokers;

    public StatisticResponse assemble(Statistic statistic, StatisticValueQuery statisticValueQuery) {
        List<? extends StatisticValueResponse> statisticValueResponses = statisticValueQueryInvokers.stream()
                .filter(invoker -> invoker.isApplicable(statisticValueQuery))
                .findFirst()
                .map(invoker -> invoker.apply(statistic, statisticValueQuery))
                .orElse(null);

        switch (statistic.getStatisticType()) {
            case ONE_STRING: {
                OneValueStringStatistic typedStatistic = (OneValueStringStatistic) statistic;
                return new StatisticResponse(
                        String.valueOf(statistic.getId()),
                        statistic.getName(),
                        statistic.getStatisticType(),
                        statistic.getStatisticPrivacyMode(),
                        statisticValueResponses);
            }
            case MULTIPLE_ENUMS: {
                MultipleValueEnumStatistic typedStatistic = (MultipleValueEnumStatistic) statistic;
                return new MultipleEnumsStatisticResponse(
                        String.valueOf(statistic.getId()),
                        statistic.getName(),
                        statistic.getStatisticType(),
                        statistic.getStatisticPrivacyMode(),
                        typedStatistic.getStatisticEnumDefinitions().stream()
                                .map(StatisticEnumDefinitionDTO::new).collect(Collectors.toList()),
                        statisticValueResponses);
            }
            case MULTIPLE_FLOATS: {
                MultipleValueFloatStatistic typedStatistic = (MultipleValueFloatStatistic) statistic;
                return new MultipleFloatsStatisticResponse(
                        String.valueOf(statistic.getId()),
                        statistic.getName(),
                        statistic.getStatisticType(),
                        statistic.getStatisticPrivacyMode(),
                        typedStatistic.getMetric(),
                        statisticValueResponses);
            }
        }
        return null;
    }

}
