package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class StationClientAuthenticationRequiredException extends BaseErrorException {
    public StationClientAuthenticationRequiredException() {
        super(FORBIDDEN,
                "STATION_CLIENT_REQUIRED",
                "Only station can use this API.");
    }
}
