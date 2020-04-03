package pl.edu.agh.airsystem.model.sensors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.database.Sensor;

@Getter
@AllArgsConstructor
public class NewSensorResponse {
    private final long id;

    public NewSensorResponse(Sensor sensor) {
        this.id = sensor.getId();
    }

}