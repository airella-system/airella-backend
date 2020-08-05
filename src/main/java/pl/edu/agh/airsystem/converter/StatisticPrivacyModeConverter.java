package pl.edu.agh.airsystem.converter;

import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatisticPrivacyModeConverter implements AttributeConverter<StatisticPrivacyMode, String> {

    public static String convertEnumToString(StatisticPrivacyMode statisticPrivacyMode) {
        return statisticPrivacyMode.getCode();
    }

    public static StatisticPrivacyMode convertStringToEnum(String statisticPrivacyModeFromDb) {
        for (StatisticPrivacyMode sensorType : StatisticPrivacyMode.values()) {
            if (sensorType.getCode().equals(statisticPrivacyModeFromDb)) {
                return sensorType;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + statisticPrivacyModeFromDb);
    }

    @Override
    public String convertToDatabaseColumn(StatisticPrivacyMode statisticPrivacyMode) {
        return convertEnumToString(statisticPrivacyMode);
    }

    @Override
    public StatisticPrivacyMode convertToEntityAttribute(String statisticPrivacyModeFromDb) {
        return convertStringToEnum(statisticPrivacyModeFromDb);
    }

}