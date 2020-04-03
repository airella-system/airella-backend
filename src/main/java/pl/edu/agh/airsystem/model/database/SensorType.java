package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SensorType {
    PM1("pm1"),
    PM10("pm10"),
    PM25("pm25"),
    TEMP("temperature"),
    HUMIDITY("humidity");

    private final String code;
}
