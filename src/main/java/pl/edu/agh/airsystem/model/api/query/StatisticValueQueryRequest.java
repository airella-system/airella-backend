package pl.edu.agh.airsystem.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticValueQueryRequest {
    private String timespan;
    private String interval;
    private String strategy;
}
