package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueStatistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;

@Service
@Transactional
@AllArgsConstructor
public class StatisticValueUtilsService {
    private StatisticValueRepository statisticValueRepository;
    private StatisticRepository statisticRepository;

    public void addNewStatisticValue(long statisticsId, StatisticValue statisticValue) {
        MultipleValueStatistic multipleValueStatistic = (MultipleValueStatistic) statisticRepository.findById(statisticsId).get();
        statisticValue.setStatistic(multipleValueStatistic);
        multipleValueStatistic.getValues().add(statisticValue);
        statisticValueRepository.save(statisticValue);
        statisticRepository.save(multipleValueStatistic);
    }

}
