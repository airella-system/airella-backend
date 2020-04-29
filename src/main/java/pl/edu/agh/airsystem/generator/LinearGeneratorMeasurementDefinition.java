package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.repository.MeasurementRepository;
import pl.edu.agh.airsystem.repository.SensorRepository;
import pl.edu.agh.airsystem.util.SensorUtilsService;

import java.time.Duration;

@AllArgsConstructor
public class LinearGeneratorMeasurementDefinition
        implements GeneratorMeasurementDefinition {
    private final double min;
    private final double max;
    private final double minStep;
    private final double maxStep;
    private final Duration stepAfterMin;
    private final Duration stepAfterMax;

    @Override
    public void startMeasurementsGenerator(Sensor sensor,
                                           SensorRepository sensorRepository,
                                           SensorUtilsService sensorUtilsService,
                                           MeasurementRepository repository,
                                           TaskScheduler taskScheduler) {
        new LinearGeneratorMeasurement(min, max, minStep, maxStep, stepAfterMin, stepAfterMax)
                .startMeasurementsGenerator(sensor, sensorRepository, sensorUtilsService, repository, taskScheduler);
    }

}
