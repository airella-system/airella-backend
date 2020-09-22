package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatisticPrivacyMode {
    PRIVATE("PRIVATE"), PUBLIC("PUBLIC");

    private final String code;
}
