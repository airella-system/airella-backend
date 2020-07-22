package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SensorType {
    PM1("pm1"),
    PM2_5("pm2_5"),
    PM10("pm10"),
    TEMP("temperature"),
    PRESSURE("pressure"),
    HUMIDITY("humidity");

    private final String code;
}
