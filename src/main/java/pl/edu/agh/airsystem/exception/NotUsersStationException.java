package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class NotUsersStationException extends BaseErrorException {
    public NotUsersStationException() {
        super(BAD_REQUEST,
                "NOT_USERS_STATION",
                "This station isn't yours.");
    }
}
