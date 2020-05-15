package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class EmailServiceErrorException extends BaseErrorException {
    public EmailServiceErrorException() {
        super(INTERNAL_SERVER_ERROR,
                "EMAIL_SERVICE_ERROR",
                "Our email service doesn't work.");
    }
}
