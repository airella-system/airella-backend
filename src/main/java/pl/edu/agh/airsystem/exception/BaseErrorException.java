package pl.edu.agh.airsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.edu.agh.airsystem.model.api.response.ErrorResponse;

@Getter
public class BaseErrorException extends RuntimeException {
    private final ErrorResponse errorResponse;
    private final HttpStatus httpStatus;

    public BaseErrorException(HttpStatus httpStatus, String code, String text) {
        this.httpStatus = httpStatus;
        this.errorResponse = new ErrorResponse(httpStatus.value(), code, text);
    }

}
