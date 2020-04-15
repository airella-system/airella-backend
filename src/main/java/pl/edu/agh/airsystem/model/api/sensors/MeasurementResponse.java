package pl.edu.agh.airsystem.model.api.sensors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.Measurement;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementResponse {
    private LocalDateTime timestamp;
    private double value;

    public MeasurementResponse(Measurement measurement) {
        this.timestamp = measurement.getTimestamp();
        this.value = measurement.getValue();
    }

}
