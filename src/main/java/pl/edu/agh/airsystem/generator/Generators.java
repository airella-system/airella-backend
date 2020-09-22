package pl.edu.agh.airsystem.generator;

import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.statistic.StatisticEnumDefinition;
import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;
import pl.edu.agh.airsystem.model.database.statistic.StatisticType;
import pl.edu.agh.airsystem.util.GeneratorUtils;
import pl.edu.agh.airsystem.util.RandomUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.util.RandomUtils.randomBetween;

public class Generators {
    public static List<GeneratorSensorDefinition> standardSensors =
            List.of(new GeneratorSensorDefinition(
                            SensorType.PM1.getCode(), "PM1", SensorType.PM1,
                            new LinearGeneratorMeasurementDefinition(0, 50,
                                    5, 10, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.PM10.getCode(), "PM10", SensorType.PM10,
                            new LinearGeneratorMeasurementDefinition(0, 200,
                                    5, 10, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.PM2_5.getCode(), "PM2_5", SensorType.PM2_5,
                            new LinearGeneratorMeasurementDefinition(0, 200,
                                    5, 10, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.TEMP.getCode(), "TEMP", SensorType.TEMP,
                            new LinearGeneratorMeasurementDefinition(-10, 30,
                                    5, 10, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.PRESSURE.getCode(), "PRESSURE", SensorType.PRESSURE,
                            new LinearGeneratorMeasurementDefinition(95000, 105000,
                                    50, 100, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.HUMIDITY.getCode(), "HUMIDITY", SensorType.HUMIDITY,
                            new LinearGeneratorMeasurementDefinition(0, 100,
                                    5, 10, Duration.ofMinutes(10), Duration.ofMinutes(60))));

    public static List<GeneratorStatisticDefinition> standardStatistics =
            List.of(new GeneratorStatisticDefinition(
                            "voltage", "Voltage", StatisticType.MULTIPLE_FLOATS,
                            StatisticPrivacyMode.PUBLIC, "V", null,
                            new LinearMultipleDoubleValueStatisticGeneratorDefinition(0, 100,
                                    5, 10, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorStatisticDefinition(
                            "internet", "Internet", StatisticType.MULTIPLE_ENUMS,
                            StatisticPrivacyMode.PUBLIC, null,
                            new ArrayList<>(List.of(new StatisticEnumDefinition("GSM", "GSM"),
                                    new StatisticEnumDefinition("WIFI", "WiFi"),
                                    new StatisticEnumDefinition("NONE", "None"))),
                            new LinearMultipleStringValueStatisticGeneratorDefinition(List.of("GSM", "WiFi", "NONE"),
                                    Duration.ofMinutes(10), Duration.ofMinutes(60)))
            );


    public static List<GeneratorStationDefinition> generatorStationDefinitions = List.of();

    public static List<GeneratorStationDefinition> getGeneratorStationDefinitions() {
        List<GeneratorStationDefinition> stationDefinitions =
                new ArrayList<>(generatorStationDefinitions);

        int NUM_OF_STATIONS = 100;

        Location from = new Location(50.137422, 19.783417);
        Location to = new Location(49.972368, 20.135403);

        for (int i = 0; i < NUM_OF_STATIONS; i++) {
            Location location = new Location(
                    randomBetween(from.getLatitude(), to.getLatitude()),
                    randomBetween(from.getLongitude(), to.getLongitude()));
            Address address = new Address("Poland", "Kraków",
                    GeneratorUtils.generateString(), String.valueOf(RandomUtils.randomBetween(1, 200)));
            stationDefinitions.add(new GeneratorStationDefinition(
                    "gen-" + i, location, address, standardSensors, standardStatistics));
        }

        return stationDefinitions;
    }

}
