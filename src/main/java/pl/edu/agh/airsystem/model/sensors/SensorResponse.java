package pl.edu.agh.airsystem.model.sensors;

import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorValue;

import java.util.Comparator;
import java.util.Optional;

public class SensorResponse extends Sensor {
    private double value;
    private String type;

    public SensorResponse(Sensor sensor) {
        type = sensor.getType().getCode();
        Optional<SensorValue> sensorValue = sensor.getValues().stream().max(Comparator.comparing(SensorValue::getTimestamp));
        value = sensorValue.get().getValue();
    }
}
