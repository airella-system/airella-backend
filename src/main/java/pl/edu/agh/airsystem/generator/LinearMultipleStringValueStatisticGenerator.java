package pl.edu.agh.airsystem.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValueString;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static pl.edu.agh.airsystem.util.RandomUtils.randomBetween;

@Slf4j
public class LinearMultipleStringValueStatisticGenerator
        implements GeneratorStatisticValueInstance {
    private List<String> choices;
    private final Duration stepAfterMin;
    private final Duration stepAfterMax;
    private int direction = 1;
    private String currentValue;

    public LinearMultipleStringValueStatisticGenerator(List<String> choices,
                                                       Duration stepAfterMin, Duration stepAfterMax) {
        this.choices = choices;
        this.stepAfterMin = stepAfterMin;
        this.stepAfterMax = stepAfterMax;
        currentValue = choices.get(randomBetween(0, choices.size() - 1));
    }

    public Duration getTimeStep() {
        return Duration.ofSeconds((long) randomBetween(stepAfterMin.getSeconds(), stepAfterMax.getSeconds()));
    }


    private void generateNextValue() {
        currentValue = choices.get(randomBetween(0, choices.size() - 1));
    }

    void generateAndAddNewStatisticValue(StatisticValueUtilsService statisticValueUtilsService,
                                         long statisticDbId, Instant current) {
        StatisticValueString statisticValueString = new StatisticValueString(null, current, currentValue);
        statisticValueUtilsService.addNewStatisticValue(statisticDbId, statisticValueString);
    }

    @Override
    public void catchUpStatistics(long statisticDbId,
                                  StatisticUtilsService statisticUtilsService,
                                  StatisticRepository statisticRepository,
                                  StatisticValueUtilsService statisticValueUtilsService,
                                  StatisticValueRepository statisticValueRepository,
                                  TaskScheduler taskScheduler) {
        Instant from;
        Instant current;
        Instant to = Instant.now();

        Optional<StatisticValue> statisticValue = statisticUtilsService.findLatestMeasurementInSensor(statisticDbId);
        if (statisticValue.isPresent()) {
            from = statisticValue.get().getTimestamp();
        } else {
            from = Instant.now().minus(1, ChronoUnit.DAYS);
        }
        current = Instant.from(from).plus(getTimeStep());
        while (current.isBefore(to)) {
            generateNextValue();
            generateAndAddNewStatisticValue(statisticValueUtilsService, statisticDbId, current);
            current = current.plus(getTimeStep());
        }
    }

    @Override
    public void startStatisticsGenerator(long statisticDbId,
                                         StatisticUtilsService statisticUtilsService,
                                         StatisticRepository statisticRepository,
                                         StatisticValueUtilsService statisticValueUtilsService,
                                         StatisticValueRepository statisticValueRepository,
                                         TaskScheduler taskScheduler) {
        scheduleNextIteration(statisticDbId, statisticUtilsService, statisticRepository, statisticValueUtilsService, statisticValueRepository, taskScheduler);
    }

    private void generatorIteration(long statisticDbId,
                                    StatisticUtilsService statisticUtilsService,
                                    StatisticRepository statisticRepository,
                                    StatisticValueUtilsService statisticValueUtilsService,
                                    StatisticValueRepository statisticValueRepository,
                                    TaskScheduler taskScheduler) {
        generateNextValue();
        generateAndAddNewStatisticValue(statisticValueUtilsService, statisticDbId, Instant.now());
        scheduleNextIteration(statisticDbId, statisticUtilsService, statisticRepository, statisticValueUtilsService, statisticValueRepository, taskScheduler);
    }

    private void scheduleNextIteration(long statisticDbId,
                                       StatisticUtilsService statisticUtilsService,
                                       StatisticRepository statisticRepository,
                                       StatisticValueUtilsService statisticValueUtilsService,
                                       StatisticValueRepository statisticValueRepository,
                                       TaskScheduler taskScheduler) {
        Instant now = Instant.now();
        Instant next = now.plus(getTimeStep());

        taskScheduler.schedule(() -> generatorIteration(statisticDbId, statisticUtilsService, statisticRepository, statisticValueUtilsService, statisticValueRepository, taskScheduler),
                next.atZone(ZoneId.systemDefault()).toInstant());
    }

}
