package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailAlreadyUsedException extends BaseErrorException {
    public EmailAlreadyUsedException() {
        super(BAD_REQUEST,
                "EMAIL_USED",
                "This email is already used.");
    }
}
