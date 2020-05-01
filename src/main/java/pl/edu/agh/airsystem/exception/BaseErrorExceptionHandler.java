package pl.edu.agh.airsystem.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseErrorExceptionHandler {

    @ExceptionHandler(value = BaseErrorException.class)
    public ResponseEntity<Object> exception(BaseErrorException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(exception.getErrorResponse());
    }

}