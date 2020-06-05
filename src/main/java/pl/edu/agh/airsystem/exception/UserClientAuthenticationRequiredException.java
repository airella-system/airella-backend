package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class UserClientAuthenticationRequiredException extends BaseErrorException {
    public UserClientAuthenticationRequiredException() {
        super(FORBIDDEN,
                "USER_CLIENT_REQUIRED",
                "Only user can user this API.");
    }
}
