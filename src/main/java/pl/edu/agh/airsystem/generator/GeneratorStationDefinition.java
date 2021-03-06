package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;

import java.util.List;

@Getter
@AllArgsConstructor
public class GeneratorStationDefinition {
    private String name;
    private Location location;
    private Address address;
    private List<GeneratorSensorDefinition> generatorSensorDefinitions;
    private List<GeneratorStatisticDefinition> GeneratorStatisticDefinitions;

}
