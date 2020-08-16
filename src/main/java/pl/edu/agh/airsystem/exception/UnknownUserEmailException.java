package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UnknownUserEmailException extends BaseErrorException {
    public UnknownUserEmailException() {
        super(BAD_REQUEST,
                "UNKNOWN_USER_EMAIL",
                "User with this email doesn't exist.");
    }
}
