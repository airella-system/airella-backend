package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueStatistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;

@Service
@AllArgsConstructor
public class StatisticValueUtilsService {
    private StatisticValueRepository statisticValueRepository;
    private StatisticRepository statisticRepository;

    public void addNewStatisticValue(MultipleValueStatistic statistic, StatisticValue statisticValue) {
        statistic.setLatestStatisticValue(statisticValue);
        statistic.getValues().add(statisticValue);
        statisticValueRepository.save(statisticValue);
        statisticRepository.save(statistic);
    }

}
