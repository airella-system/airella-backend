package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatisticChartType {
    LINE("LINE"),
    SCATTER("SCATTER");

    private final String code;

}
