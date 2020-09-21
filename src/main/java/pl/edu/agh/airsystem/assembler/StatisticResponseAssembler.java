package pl.edu.agh.airsystem.assembler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.statistic.StatisticResponse;
import pl.edu.agh.airsystem.model.api.statistic.StatisticValueResponse;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.service.statistic.queryinvoker.StatisticValueQueryInvoker;

import java.util.List;

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

        return new StatisticResponse(
                String.valueOf(statistic.getId()),
                statistic.getStatisticType(),
                statistic.getStatisticPrivacyMode(),
                statisticValueResponses);
    }

}
