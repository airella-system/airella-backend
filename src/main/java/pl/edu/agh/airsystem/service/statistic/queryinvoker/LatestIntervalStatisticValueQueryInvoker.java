package pl.edu.agh.airsystem.service.statistic.queryinvoker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.sensors.TimespanResponse;
import pl.edu.agh.airsystem.model.api.statistic.IntervalStatisticValueResponse;
import pl.edu.agh.airsystem.model.api.statistic.IntervalTimeStatisticValueResponse;
import pl.edu.agh.airsystem.model.api.statistic.StatisticValueResponse;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;
import pl.edu.agh.airsystem.util.Interval;

import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.model.api.query.StatisticValueQueryStrategy.LATEST;
import static pl.edu.agh.airsystem.util.Intervals.generateIntervals;

@Component
@AllArgsConstructor
public class LatestIntervalStatisticValueQueryInvoker implements StatisticValueQueryInvoker {
    private final StatisticValueRepository statisticValueRepository;

    @Override
    public List<? extends StatisticValueResponse> apply(Statistic statistic, StatisticValueQuery statisticValueQueryRequest) {
        List<Interval> intervals = generateIntervals(statisticValueQueryRequest.getTimespan(),
                statisticValueQueryRequest.getInterval());

        List<IntervalStatisticValueResponse> statisticValueResponses = new ArrayList<>();
        for (Interval interval : intervals) {
            TimespanResponse timespan = new TimespanResponse(
                    interval.getStart(),
                    interval.getEnd());

            statisticValueRepository.findFirstByStatisticAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(statistic, interval.getStart(), interval.getEnd())
                    .ifPresentOrElse(e -> statisticValueResponses.add(
                            new IntervalTimeStatisticValueResponse(timespan, e.getTimestamp().toString(), e.getValue())),
                            () -> statisticValueResponses.add(
                                    new IntervalTimeStatisticValueResponse(timespan, null, null)));
        }
        return statisticValueResponses;
    }

    @Override
    public boolean isApplicable(StatisticValueQuery statisticValueQueryRequest) {
        return statisticValueQueryRequest.getStrategy() == LATEST &&
                statisticValueQueryRequest.getTimespan() != null &&
                statisticValueQueryRequest.getInterval() != null;
    }

}
