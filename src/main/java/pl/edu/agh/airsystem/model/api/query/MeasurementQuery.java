package pl.edu.agh.airsystem.model.api.query;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.SensorType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MeasurementQuery {

    LocalDateTime startDate;
    LocalDateTime endDate;
    Duration interval;
    List<SensorType> types;

    public MeasurementQuery(MeasurementQueryRequest measurementQueryRequest) {
    }

}
