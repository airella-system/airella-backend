package pl.edu.agh.airsystem.service.statistic.queryinvoker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.statistic.SingleStatisticValueResponse;
import pl.edu.agh.airsystem.model.api.statistic.StatisticValueResponse;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueEnumStatistic;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueFloatStatistic;
import pl.edu.agh.airsystem.model.database.statistic.OneValueStringStatistic;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;

import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.model.api.query.StatisticValueQueryStrategy.LATEST;


@Component
@AllArgsConstructor
public class LatestStatisticValueQueryInvoker implements StatisticValueQueryInvoker {

    @Override
    public List<? extends StatisticValueResponse> apply(Statistic statistic, StatisticValueQuery statisticValueQueryRequest) {
        List<StatisticValueResponse> statisticValueResponses = new ArrayList<>();
        StatisticValue statisticValue = null;

        switch (statistic.getStatisticType()) {
            case ONE_STRING:
                statisticValue = ((OneValueStringStatistic) statistic).getValue();
                break;
            case MULTIPLE_ENUMS:
                statisticValue = ((MultipleValueEnumStatistic) statistic).getLatestStatisticValue();
                break;
            case MULTIPLE_FLOATS:
                statisticValue = ((MultipleValueFloatStatistic) statistic).getLatestStatisticValue();
                break;
        }

        statisticValueResponses.add(new SingleStatisticValueResponse(statisticValue));

        return statisticValueResponses;
    }

    @Override
    public boolean isApplicable(StatisticValueQuery statisticValueQueryRequest) {
        return statisticValueQueryRequest.getStrategy() == LATEST &&
                statisticValueQueryRequest.getTimespan() == null &&
                statisticValueQueryRequest.getInterval() == null;
    }

}
