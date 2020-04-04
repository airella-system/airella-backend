package pl.edu.agh.airsystem.model.sensors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewSensorResponse {
    private final long id;

    public NewSensorResponse(int id) {
        this.id = id;
    }

}