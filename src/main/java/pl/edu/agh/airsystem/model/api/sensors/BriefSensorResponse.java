package pl.edu.agh.airsystem.model.api.sensors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;

import java.util.Comparator;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BriefSensorResponse {
    private SensorType type;
    private Double value;

    public BriefSensorResponse(Sensor sensor) {
        this.type = sensor.getType();
        Optional<Measurement> sensorValue = sensor.getMeasurements().stream()
                .max(Comparator.comparing(Measurement::getTimestamp));

        if (sensorValue.isPresent()) {
            value = sensorValue.get().getValue();
        } else {
            value = null;
        }
    }
}
