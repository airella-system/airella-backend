package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.repository.MeasurementRepository;
import pl.edu.agh.airsystem.repository.SensorRepository;
import pl.edu.agh.airsystem.repository.StationRepository;
import pl.edu.agh.airsystem.util.SensorUtilsService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class Generator {
    @Qualifier("generatorTaskScheduler")
    TaskScheduler taskScheduler;
    private StationRepository stationRepository;
    private SensorRepository sensorRepository;
    private MeasurementRepository measurementRepository;
    private SensorUtilsService sensorUtilsService;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        event.getApplicationContext().getBean(Generator.class).postConstruct();
    }

    @Transactional
    public void postConstruct() {
        log.info("Generator is working...");

        List<GeneratorStationDefinition> generatorStationDefinitions =
                Generators.getGeneratorStationDefinitions();

        for (GeneratorStationDefinition stationDefinition : generatorStationDefinitions) {
            log.info("Generator is working on station: " + stationDefinition.getName());

            Station station = getOrCreateStation(stationDefinition);
            for (GeneratorSensorDefinition sensorDefinition : stationDefinition.getGeneratorSensorDefinitions()) {
                executeSensorGenerator(station, sensorDefinition);
            }
        }
        log.info("Generator has ended it's job!");
    }

    private void executeSensorGenerator(Station station, GeneratorSensorDefinition generatorSensorDefinition) {
        Sensor sensor = getOrCreateSensor(station, generatorSensorDefinition);
        generatorSensorDefinition.getGeneratorMeasurementDefinition().startMeasurementsGenerator(
                sensor, sensorRepository, sensorUtilsService, measurementRepository, taskScheduler);
    }

    private Sensor getOrCreateSensor(Station station, GeneratorSensorDefinition generatorSensorDefinition) {
        Optional<Sensor> foundSensor = station.getSensors().stream()
                .filter(sensor -> sensor.getId().equals(generatorSensorDefinition.getId()))
                .findFirst();
        if (foundSensor.isPresent()) {
            return foundSensor.get();
        } else {
            Sensor sensor = new Sensor();
            sensor.setStation(station);
            sensor.setId(generatorSensorDefinition.getId());
            sensor.setType(generatorSensorDefinition.getType());
            sensor.setMeasurements(new HashSet<>());
            sensorRepository.save(sensor);
            return sensor;
        }
    }

    private Station getOrCreateStation(GeneratorStationDefinition stationDefinition) {
        List<Station> stations = stationRepository.findByName(stationDefinition.getName());
        if (stations.size() == 0) {
            Station station = new Station();
            station.setName(stationDefinition.getName());
            station.setLocation(stationDefinition.getLocation());
            stationRepository.save(station);
            return station;
        } else {
            return stations.get(0);
        }
    }

}
