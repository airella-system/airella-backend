package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueStatistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StatisticUtilsService {

    public Optional<StatisticValue> findLatestMeasurementInSensor(MultipleValueStatistic statistic) {
        return Optional.ofNullable(statistic.getLatestStatisticValue());
    }

}
