package pl.edu.agh.airsystem.generator;

import org.springframework.scheduling.TaskScheduler;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;
import pl.edu.agh.airsystem.util.StatisticUtilsService;
import pl.edu.agh.airsystem.util.StatisticValueUtilsService;

public interface GeneratorStatisticValueInstance {
    void catchUpStatistics(long statisticDbId,
                           StatisticUtilsService statisticUtilsService,
                           StatisticRepository statisticRepository,
                           StatisticValueUtilsService statisticValueUtilsService,
                           StatisticValueRepository statisticValueRepository,
                           TaskScheduler taskScheduler);

    void startStatisticsGenerator(long statisticDbId,
                                  StatisticUtilsService statisticUtilsService,
                                  StatisticRepository statisticRepository,
                                  StatisticValueUtilsService statisticValueUtilsService,
                                  StatisticValueRepository statisticValueRepository,
                                  TaskScheduler taskScheduler);
}
