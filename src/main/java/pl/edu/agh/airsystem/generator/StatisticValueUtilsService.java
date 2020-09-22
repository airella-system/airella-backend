package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueEnumStatistic;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueFloatStatistic;
import pl.edu.agh.airsystem.model.database.statistic.OneValueStringStatistic;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;

@Service
@Transactional
@AllArgsConstructor
public class StatisticValueUtilsService {
    private StatisticValueRepository statisticValueRepository;
    private StatisticRepository statisticRepository;

    public void addNewStatisticValue(long statisticId, StatisticValue statisticValue) {
        Statistic statistic = statisticRepository.findById(statisticId).get();

        switch (statistic.getStatisticType()) {
            case ONE_STRING:
                ((OneValueStringStatistic) statistic).setValue(statisticValue);
                break;
            case MULTIPLE_ENUMS:
                ((MultipleValueEnumStatistic) statistic).getValues().add(statisticValue);
                ((MultipleValueEnumStatistic) statistic).setLatestStatisticValue(statisticValue);
                break;
            case MULTIPLE_FLOATS:
                ((MultipleValueFloatStatistic) statistic).getValues().add(statisticValue);
                ((MultipleValueFloatStatistic) statistic).setLatestStatisticValue(statisticValue);
                break;
        }

        statisticValue.setStatistic(statistic);
        statisticValueRepository.save(statisticValue);
        statisticRepository.save(statistic);
    }

}
