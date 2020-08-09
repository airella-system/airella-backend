package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatisticType {
    ONE_STRING_VALUE("one_string_value", String.class, false),
    ONE_DOUBLE_VALUE("one_double_value", Double.class, false),
    MULTI_STRING_VALUE("multi_string_value", String.class, true),
    MULTI_DOUBLE_AGGREGATABLE_VALUE("multi_double_aggregatable_value", Double.class, true),
    MULTI_DOUBLE_VALUE("multi_double_value", Double.class, true);

    private final String code;
    private final Class<?> typeClass;
    private final boolean areMultipleValues;

}
