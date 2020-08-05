package pl.edu.agh.airsystem.converter;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQueryRequest;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQueryStrategy;
import pl.edu.agh.airsystem.util.Timespan;

import java.time.Duration;
import java.time.Instant;

import static pl.edu.agh.airsystem.model.api.query.StatisticValueQueryStrategy.LATEST;

@Component
public class StatisticValueQueryConverter implements Converter<StatisticValueQueryRequest, StatisticValueQuery> {

    @Override
    public StatisticValueQuery convert(StatisticValueQueryRequest measurementQueryRequest) {
        Timespan timespan = null;
        Duration interval = null;
        StatisticValueQueryStrategy strategy = LATEST;

        if (measurementQueryRequest.getTimespan() != null) {
            Instant startDate = Instant.parse(measurementQueryRequest.getTimespan().split("/")[0]);
            Instant endDate = Instant.parse(measurementQueryRequest.getTimespan().split("/")[1]);
            timespan = new Timespan(startDate, endDate);
        }

        if (measurementQueryRequest.getInterval() != null) {
            interval = Duration.parse(measurementQueryRequest.getInterval());
        }

        if (measurementQueryRequest.getStrategy() != null) {
            strategy = StatisticValueQueryStrategyConverter.
                    convertStringToEnum(measurementQueryRequest.getStrategy());
        }

        return new StatisticValueQuery(timespan, interval, strategy);
    }
}
