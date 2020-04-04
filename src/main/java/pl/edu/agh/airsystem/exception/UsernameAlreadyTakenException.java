package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UsernameAlreadyTakenException extends BaseErrorException {
    public UsernameAlreadyTakenException() {
        super(BAD_REQUEST,
                "USERNAME_TAKEN",
                "This username is already taken.");
    }
}
