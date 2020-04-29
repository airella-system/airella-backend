package pl.edu.agh.airsystem.generator;

import org.springframework.scheduling.TaskScheduler;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.repository.MeasurementRepository;
import pl.edu.agh.airsystem.repository.SensorRepository;
import pl.edu.agh.airsystem.util.MeasurementUtilsService;
import pl.edu.agh.airsystem.util.SensorUtilsService;

public interface GeneratorMeasurementInstance {
    void catchUpMeasurements(Sensor sensor,
                             SensorRepository sensorRepository,
                             SensorUtilsService sensorUtilsService,
                             MeasurementRepository repository,
                             MeasurementUtilsService measurementUtilsService,
                             TaskScheduler taskScheduler);

    void startMeasurementsGenerator(Sensor sensor,
                                    SensorRepository sensorRepository,
                                    SensorUtilsService sensorUtilsService,
                                    MeasurementRepository repository,
                                    MeasurementUtilsService measurementUtilsService,
                                    TaskScheduler taskScheduler);
}
