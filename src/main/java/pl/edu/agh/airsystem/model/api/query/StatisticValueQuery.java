package pl.edu.agh.airsystem.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.util.Timespan;

import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
public class StatisticValueQuery {
    private Timespan timespan;
    private Duration interval;
    private StatisticValueQueryStrategy strategy;
}
