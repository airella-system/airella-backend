package pl.edu.agh.airsystem.service.statistic.queryinvoker;

import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.statistic.StatisticValueResponse;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;

import java.util.List;

public interface StatisticValueQueryInvoker {
    List<? extends StatisticValueResponse> apply(Statistic statistic, StatisticValueQuery statisticValueQueryRequest);

    boolean isApplicable(StatisticValueQuery statisticValueQueryRequest);
}
