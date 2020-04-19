package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class WrongCredentialsException extends BaseErrorException {
    public WrongCredentialsException() {
        super(UNAUTHORIZED,
                "WRONG_CREDENTIALS",
                "Wrong username and/or password");
    }
}
