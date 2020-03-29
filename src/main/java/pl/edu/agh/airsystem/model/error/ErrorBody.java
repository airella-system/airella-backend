package pl.edu.agh.airsystem.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ErrorBody {
    private Error error;

    public ErrorBody(String code, String message) {
        error = new Error(code, message);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Error {
        private String code;
        private String message;
    }
}

