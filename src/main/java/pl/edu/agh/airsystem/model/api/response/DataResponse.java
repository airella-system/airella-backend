package pl.edu.agh.airsystem.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DataResponse extends Response {
    Object data;

    public DataResponse(Object data) {
        super(true);
        this.data = data;
    }

    public static DataResponse of(Object object) {
        return new DataResponse(object);
    }

}
