package pl.edu.agh.airsystem.model.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorBody {
    private List<Error> errors = new ArrayList<>();

    public ErrorBody(int status, String title, String detail) {
        errors.add(new Error(String.valueOf(status), title, detail));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Error {
        private String status;
        private String title;
        private String detail;
    }

}

