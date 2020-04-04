package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class StationClientAuthenticationRequiredException extends BaseErrorException {
    public StationClientAuthenticationRequiredException() {
        super(BAD_REQUEST,
                "STATION_CLIENT_REQUIRED",
                "Only station can user this API.");
    }
}
