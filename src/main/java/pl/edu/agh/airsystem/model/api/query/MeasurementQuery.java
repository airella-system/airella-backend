package pl.edu.agh.airsystem.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.SensorType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MeasurementQuery {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Duration interval;
    private List<SensorType> types;
}
