package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueEnumStatistic;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueFloatStatistic;
import pl.edu.agh.airsystem.model.database.statistic.OneValueStringStatistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.repository.StatisticRepository;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class StatisticUtilsService {

    private final StatisticRepository statisticRepository;

    public Optional<StatisticValue> findLatestMeasurementInSensor(long statisticId) {
        return statisticRepository.findById(statisticId).map(statistic -> {
            switch (statistic.getStatisticType()) {
                case ONE_STRING:
                    return ((OneValueStringStatistic) statistic).getValue();
                case MULTIPLE_ENUMS:
                    return ((MultipleValueEnumStatistic) statistic).getLatestStatisticValue();
                case MULTIPLE_FLOATS:
                    return ((MultipleValueFloatStatistic) statistic).getLatestStatisticValue();
            }
            return null;
        });
    }
}
