package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatisticPrivacyMode {
    PRIVATE("private"), PUBLIC("public");

    private final String code;
}
