package pl.edu.agh.airsystem.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse extends Response {
    private List<Error> errors = new ArrayList<>();

    public ErrorResponse(int status, String title, String detail) {
        super(false);
        errors.add(new Error(String.valueOf(status), title, detail));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", isSuccess());
        map.put("errors", errors.stream().map(Error::toMap).toArray());
        return map;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Error {
        private String status;
        private String title;
        private String detail;

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("status", status);
            map.put("title", title);
            map.put("detail", detail);
            return map;
        }
    }

}

