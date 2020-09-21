package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueStatistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.repository.StatisticRepository;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class StatisticUtilsService {

    private final StatisticRepository statisticRepository;

    public Optional<StatisticValue> findLatestMeasurementInSensor(long statisticId) {
        return statisticRepository.findById(statisticId)
                .map(it -> ((MultipleValueStatistic) it).getLatestStatisticValue());
    }

}
