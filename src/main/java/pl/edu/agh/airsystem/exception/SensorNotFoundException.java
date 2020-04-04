package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class SensorNotFoundException extends BaseErrorException {
    public SensorNotFoundException() {
        super(BAD_REQUEST,
                "SENSOR_NOT_FOUND",
                "Sensor with this id doesn't exist.");
    }
}
