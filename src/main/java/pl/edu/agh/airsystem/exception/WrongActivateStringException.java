package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class WrongActivateStringException extends BaseErrorException {
    public WrongActivateStringException() {
        super(BAD_REQUEST,
                "WRONG_ACTIVATE_STRING",
                "This activation text is wrong.");
    }
}
