package pl.edu.agh.airsystem.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValueFloat;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static pl.edu.agh.airsystem.util.RandomUtils.randomBetween;

@Slf4j
public class LinearMultipleDoubleValueStatisticGenerator
        implements GeneratorStatisticValueInstance {
    private final double min;
    private final double max;
    private final double minStep;
    private final double maxStep;
    private final Duration stepAfterMin;
    private final Duration stepAfterMax;
    private int direction = 1;
    private double currentValue;

    public LinearMultipleDoubleValueStatisticGenerator(double min, double max,
                                                       double minStep, double maxStep,
                                                       Duration stepAfterMin, Duration stepAfterMax) {
        this.min = min;
        this.max = max;
        this.minStep = minStep;
        this.maxStep = maxStep;
        this.stepAfterMin = stepAfterMin;
        this.stepAfterMax = stepAfterMax;
        currentValue = randomBetween(min, max);
    }

    public Duration getTimeStep() {
        return Duration.ofSeconds((long) randomBetween(stepAfterMin.getSeconds(), stepAfterMax.getSeconds()));
    }

    public long getValueStep() {
        return (long) randomBetween(minStep, maxStep);
    }

    private void generateNextValue() {
        currentValue += getValueStep() * direction;
        if (currentValue > max) {
            currentValue = max;
            direction = -1;
        } else if (currentValue < min) {
            currentValue = min;
            direction = 1;
        }
    }


    void generateAndAddNewStatisticValue(StatisticValueUtilsService statisticValueUtilsService,
                                         long statisticDbId, Instant current) {

        StatisticValueFloat statisticValueFloat = new StatisticValueFloat(null, current, currentValue);
        statisticValueUtilsService.addNewStatisticValue(statisticDbId, statisticValueFloat);
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
            generateAndAddNewStatisticValue(statisticValueUtilsService, statisticDbId, Instant.now());
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
