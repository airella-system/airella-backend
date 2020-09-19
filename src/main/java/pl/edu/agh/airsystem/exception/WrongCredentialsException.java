package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class WrongCredentialsException extends BaseErrorException {
    public WrongCredentialsException() {
        super(FORBIDDEN,
                "WRONG_CREDENTIALS",
                "Wrong username and/or password");
    }
}
