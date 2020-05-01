package pl.edu.agh.airsystem.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.agh.airsystem.exception.WrongCredentialsException;

@ControllerAdvice
public class AuthorizationExceptionHandler {

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Object> exception(AuthenticationException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new WrongCredentialsException().getErrorResponse());
    }

}