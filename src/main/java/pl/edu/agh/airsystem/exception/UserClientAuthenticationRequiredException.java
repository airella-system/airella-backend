package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UserClientAuthenticationRequiredException extends BaseErrorException {
    public UserClientAuthenticationRequiredException() {
        super(BAD_REQUEST,
                "USER_CLIENT_REQUIRED",
                "Only user can user this API.");
    }
}
