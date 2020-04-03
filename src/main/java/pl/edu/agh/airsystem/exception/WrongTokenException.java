package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class WrongTokenException extends BaseErrorException {
    public WrongTokenException() {
        super(BAD_REQUEST,
                "WRONG_TOKEN",
                "This token is wrong.");
    }
}
