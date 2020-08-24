package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class WrongActivateStringException extends BaseErrorException {
    public WrongActivateStringException() {
        super(BAD_REQUEST,
                "WRONG_ACTIVATION_CODE",
                "This activation code is wrong.");
    }
}
