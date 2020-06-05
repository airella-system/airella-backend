package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class NotUsersStationException extends BaseErrorException {
    public NotUsersStationException() {
        super(FORBIDDEN,
                "NOT_USERS_STATION",
                "This station isn't yours.");
    }
}
