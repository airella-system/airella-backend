package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class NewSensorIdDuplicatedException extends BaseErrorException {
    public NewSensorIdDuplicatedException() {
        super(BAD_REQUEST,
                "NEW_SENSOR_ID_DUPLICATED",
                "Sensor with this id already exists.");
    }
}
