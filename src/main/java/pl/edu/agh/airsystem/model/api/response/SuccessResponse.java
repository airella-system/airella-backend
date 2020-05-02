package pl.edu.agh.airsystem.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SuccessResponse extends Response {

    public SuccessResponse() {
        super(true);
    }

}
