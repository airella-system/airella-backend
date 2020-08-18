package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class AlreadyActivatedException extends BaseErrorException {
    public AlreadyActivatedException() {
        super(BAD_REQUEST,
                "ALREADY_ACTIVATED",
                "This user is already activated.");
    }
}
