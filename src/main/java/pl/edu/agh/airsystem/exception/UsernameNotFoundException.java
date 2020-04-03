package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UsernameNotFoundException extends BaseErrorException {
    public UsernameNotFoundException(String username) {
        super(BAD_REQUEST,
                "USERNAME_TAKEN",
                "Username " + username + " not found.");
    }
}
