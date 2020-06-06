package pl.edu.agh.airsystem.generator;

import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.util.GeneratorUtils;
import pl.edu.agh.airsystem.util.RandomUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.airsystem.util.RandomUtils.randomBetween;

public class Generators {
    public static List<GeneratorSensorDefinition> standardSensors =
            List.of(new GeneratorSensorDefinition(
                            SensorType.PM1.getCode(), SensorType.PM1,
                            new LinearGeneratorMeasurementDefinition(0, 100,
                                    1, 2, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.PM10.getCode(), SensorType.PM10,
                            new LinearGeneratorMeasurementDefinition(0, 100,
                                    1, 2, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.PM2_5.getCode(), SensorType.PM2_5,
                            new LinearGeneratorMeasurementDefinition(0, 100,
                                    1, 2, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.TEMP.getCode(), SensorType.TEMP,
                            new LinearGeneratorMeasurementDefinition(-10, 30,
                                    1, 2, Duration.ofMinutes(10), Duration.ofMinutes(60))),
                    new GeneratorSensorDefinition(
                            SensorType.HUMIDITY.getCode(), SensorType.HUMIDITY,
                            new LinearGeneratorMeasurementDefinition(0, 100,
                                    1, 2, Duration.ofMinutes(10), Duration.ofMinutes(60))));

    public static List<GeneratorStationDefinition> generatorStationDefinitions = List.of();

    public static List<GeneratorStationDefinition> getGeneratorStationDefinitions() {
        List<GeneratorStationDefinition> stationDefinitions =
                new ArrayList<>(generatorStationDefinitions);

        int NUM_OF_STATIONS = 300;

        Location from = new Location(50.137422, 19.783417);
        Location to = new Location(49.972368, 20.135403);


        for (int i = 0; i < NUM_OF_STATIONS; i++) {
            Location location = new Location(
                    randomBetween(from.getLatitude(), to.getLatitude()),
                    randomBetween(from.getLongitude(), to.getLongitude()));
            Address address = new Address("Poland", "Kraków",
                    GeneratorUtils.generateString(), String.valueOf(RandomUtils.randomBetween(1, 200)));
            stationDefinitions.add(new GeneratorStationDefinition("gen-" + i, location, address, standardSensors));
        }

        return stationDefinitions;
    }

}
