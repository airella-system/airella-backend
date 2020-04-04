package pl.edu.agh.airsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.edu.agh.airsystem.model.api.error.ErrorBody;

@Getter
public class BaseErrorException extends RuntimeException {
    private final ErrorBody errorBody;
    private final HttpStatus httpStatus;

    public BaseErrorException(HttpStatus httpStatus, String code, String text) {
        this.httpStatus = httpStatus;
        this.errorBody = new ErrorBody(code, text);
    }
}
