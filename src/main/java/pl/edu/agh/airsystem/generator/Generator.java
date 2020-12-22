package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueEnumStatistic;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueFloatStatistic;
import pl.edu.agh.airsystem.model.database.statistic.OneValueStringStatistic;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticChartType;
import pl.edu.agh.airsystem.model.database.statistic.StatisticEnumDefinition;
import pl.edu.agh.airsystem.repository.AddressRepository;
import pl.edu.agh.airsystem.repository.MeasurementRepository;
import pl.edu.agh.airsystem.repository.SensorRepository;
import pl.edu.agh.airsystem.repository.StationRepository;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;
import pl.edu.agh.airsystem.service.RegisterLoginService;
import pl.edu.agh.airsystem.util.MeasurementUtilsService;
import pl.edu.agh.airsystem.util.Pair;
import pl.edu.agh.airsystem.util.SensorUtilsService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class Generator {
    @Qualifier("generatorTaskScheduler")
    TaskScheduler taskScheduler;

    private final GeneratorUtil generatorUtil;

    private final StationRepository stationRepository;
    private final AddressRepository addressRepository;
    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final SensorUtilsService sensorUtilsService;
    private final MeasurementUtilsService measurementUtilsService;

    private final StatisticRepository statisticRepository;
    private final StatisticUtilsService statisticUtilsService;
    private final StatisticValueRepository statisticValueRepository;
    private final StatisticValueUtilsService statisticValueUtilsService;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        event.getApplicationContext().getBean(Generator.class).postConstruct();
    }

    @Transactional
    public void postConstruct() {
        if (!generatorUtil.isGeneratorEnabled()) {
            log.info("Generator disabled");
            return;
        }

        log.info("Generator is working...");

        List<GeneratorStationDefinition> generatorStationDefinitions =
                Generators.getGeneratorStationDefinitions();

        List<Pair<Sensor, GeneratorMeasurementInstance>> sensorToMeasurementGenerator = new ArrayList<>();
        List<Pair<Statistic, GeneratorStatisticValueInstance>> statisticToStatisticValueGenerator = new ArrayList<>();

        for (GeneratorStationDefinition stationDefinition : generatorStationDefinitions) {
            log.info("Generator is preparing station: " + stationDefinition.getName());

            Station station = getOrCreateStation(stationDefinition);

            for (GeneratorSensorDefinition sensorDefinition : stationDefinition.getGeneratorSensorDefinitions()) {
                Sensor sensor = getOrCreateSensor(station, sensorDefinition);
                GeneratorMeasurementInstance instance = sensorDefinition.getGeneratorMeasurementDefinition().createInstance();
                instance.catchUpMeasurements(sensor.getDbId(),
                        sensorRepository, sensorUtilsService, measurementRepository,
                        measurementUtilsService, taskScheduler);
                sensorToMeasurementGenerator.add(new Pair<>(sensor, instance));
            }

            for (GeneratorStatisticDefinition statisticDefinition : stationDefinition.getGeneratorStatisticDefinitions()) {
                Statistic statistic = getOrCreateStatistic(station, statisticDefinition);
                GeneratorStatisticValueInstance instance = statisticDefinition.getGeneratorStatisticValueDefinition().createInstance();
                instance.catchUpStatistics(statistic.getDbId(),
                        statisticUtilsService, statisticRepository, statisticValueUtilsService,
                        statisticValueRepository, taskScheduler);
                statisticToStatisticValueGenerator.add(new Pair<>(statistic, instance));
            }
        }

        for (Pair<Sensor, GeneratorMeasurementInstance> pair : sensorToMeasurementGenerator) {
            pair.getValue().startMeasurementsGenerator(pair.getKey().getDbId(),
                    sensorRepository, sensorUtilsService, measurementRepository,
                    measurementUtilsService, taskScheduler);
        }
        for (Pair<Statistic, GeneratorStatisticValueInstance> pair : statisticToStatisticValueGenerator) {
            pair.getValue().startStatisticsGenerator(pair.getKey().getDbId(),
                    statisticUtilsService, statisticRepository, statisticValueUtilsService,
                    statisticValueRepository, taskScheduler);
        }
        log.info("Generator has ended it's job!");

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
            sensor.setName(generatorSensorDefinition.getName());
            sensor.setType(generatorSensorDefinition.getType());
            sensor.setMeasurements(new HashSet<>());
            sensorRepository.save(sensor);
            station.getSensors().add(sensor);
            stationRepository.save(station);
            return sensor;
        }
    }

    private Statistic getOrCreateStatistic(Station station, GeneratorStatisticDefinition generatorStatisticDefinition) {
        Optional<Statistic> foundStatistic = station.getStatistics().stream()
                .filter(statistic -> statistic.getId().equals(generatorStatisticDefinition.getId()))
                .findFirst();
        if (foundStatistic.isPresent()) {
            return foundStatistic.get();
        } else {
            Statistic statistic;
            switch (generatorStatisticDefinition.getType()) {
                case ONE_STRING:
                    statistic = new OneValueStringStatistic(generatorStatisticDefinition.getId(),
                            generatorStatisticDefinition.getName(),
                            station,
                            generatorStatisticDefinition.getType(),
                            generatorStatisticDefinition.getPrivacyMode());
                    break;
                case MULTIPLE_ENUMS:
                    statistic = new MultipleValueEnumStatistic(generatorStatisticDefinition.getId(),
                            generatorStatisticDefinition.getName(),
                            generatorStatisticDefinition.getStatisticEnumDefinitions()
                                    .stream()
                                    .map(it -> new StatisticEnumDefinition(it.getId(), it.getName()))
                                    .collect(Collectors.toList()),
                            StatisticChartType.LINE,
                            station,
                            generatorStatisticDefinition.getType(),
                            generatorStatisticDefinition.getPrivacyMode());
                    break;
                case MULTIPLE_FLOATS:
                    statistic = new MultipleValueFloatStatistic(generatorStatisticDefinition.getId(),
                            generatorStatisticDefinition.getName(),
                            generatorStatisticDefinition.getMetric(),
                            StatisticChartType.LINE,
                            station,
                            generatorStatisticDefinition.getType(),
                            generatorStatisticDefinition.getPrivacyMode());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + generatorStatisticDefinition.getType());
            }
            statisticRepository.save(statistic);
            station.getStatistics().add(statistic);
            stationRepository.save(station);
            return statistic;
        }
    }

    private Station getOrCreateStation(GeneratorStationDefinition stationDefinition) {
        List<Station> stations = stationRepository.findByName(stationDefinition.getName());
        if (stations.size() == 0) {
            Address address = stationDefinition.getAddress();
            addressRepository.save(address);

            Station station = new Station();
            station.setId(RegisterLoginService.generateStationId());
            station.setName(stationDefinition.getName());
            station.setLocation(stationDefinition.getLocation());
            station.setAddress(stationDefinition.getAddress());
            stationRepository.save(station);
            return station;
        } else {
            return stations.get(0);
        }
    }

}
