package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailNotFoundException extends BaseErrorException {
    public EmailNotFoundException(String email) {
        super(BAD_REQUEST,
                "EMAIL_NOT_FOUND",
                "User with email " + email + " not found.");
    }
}
