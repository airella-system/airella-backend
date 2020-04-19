package pl.edu.agh.airsystem.model.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse {
    Object data;

    public static DataResponse of(Object object) {
        return new DataResponse(object);
    }

}
