package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailAlreadyUsedException extends BaseErrorException {
    public EmailAlreadyUsedException() {
        super(BAD_REQUEST,
                "USERNAME_TAKEN",
                "This username is already taken.");
    }
}
