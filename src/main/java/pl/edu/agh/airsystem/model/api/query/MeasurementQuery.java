package pl.edu.agh.airsystem.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.util.Timespan;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MeasurementQuery {
    private Timespan timespan;
    private Duration interval;
    private List<SensorType> types;
    private MeasurementQueryStrategy strategy;
    private boolean interpolate;
}
