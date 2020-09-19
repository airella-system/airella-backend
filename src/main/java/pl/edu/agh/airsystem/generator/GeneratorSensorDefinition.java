package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.database.SensorType;

@Getter
@AllArgsConstructor
public class GeneratorSensorDefinition {
    private String id;
    private String name;
    private SensorType type;
    private GeneratorMeasurementDefinition generatorMeasurementDefinition;
}
