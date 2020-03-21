package pl.edu.agh.airsystem.model.stations;

import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;

public class BriefSensorResponse {
    private final SensorType type;

    public BriefSensorResponse(Sensor sensor) {
        this.type = sensor.getType();
    }
}
