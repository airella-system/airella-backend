package pl.edu.agh.airsystem.service.statistic.queryinvoker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.statistic.OneStatisticValueResponse;
import pl.edu.agh.airsystem.model.api.statistic.StatisticValueResponse;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;

import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.agh.airsystem.model.api.query.StatisticValueQueryStrategy.ALL;

@Component
@AllArgsConstructor
public class AllValueQueryInvoker implements StatisticValueQueryInvoker {
    private final StatisticValueRepository statisticValueRepository;

    @Override
    public List<? extends StatisticValueResponse> apply(Statistic statistic, StatisticValueQuery statisticValueQueryRequest) {
        List<OneStatisticValueResponse> statisticValueResponses =
                statisticValueRepository.findAllByStatisticAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(statistic, statisticValueQueryRequest.getTimespan().getStart(), statisticValueQueryRequest.getTimespan().getEnd()).stream()
                        .map(e -> new OneStatisticValueResponse(e.getTimestamp(), e.getValue())).collect(Collectors.toList());

        return statisticValueResponses;
    }

    @Override
    public boolean isApplicable(StatisticValueQuery statisticValueQueryRequest) {
        return statisticValueQueryRequest.getStrategy() == ALL &&
                statisticValueQueryRequest.getTimespan() != null &&
                statisticValueQueryRequest.getInterval() == null;
    }

}
