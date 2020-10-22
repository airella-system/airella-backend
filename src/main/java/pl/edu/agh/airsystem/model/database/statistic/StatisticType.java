package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatisticType {
    ONE_STRING("ONE_STRING"),
    MULTIPLE_ENUMS("MULTIPLE_ENUMS"),
    MULTIPLE_FLOATS("MULTIPLE_FLOATS");

    private final String code;

}
