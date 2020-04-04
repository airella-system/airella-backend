package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class StationNotFoundException extends BaseErrorException {
    public StationNotFoundException() {
        super(BAD_REQUEST,
                "STATION_NOT_FOUND",
                "Station with this id doesn't exist.");
    }
}
