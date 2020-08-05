package pl.edu.agh.airsystem.converter;

import pl.edu.agh.airsystem.model.api.query.StatisticValueQueryStrategy;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatisticValueQueryStrategyConverter implements AttributeConverter<StatisticValueQueryStrategy, String> {

    public static String convertEnumToString(StatisticValueQueryStrategy sensorType) {
        return sensorType.getCode();
    }

    public static StatisticValueQueryStrategy convertStringToEnum(String text) {
        for (StatisticValueQueryStrategy sensorType : StatisticValueQueryStrategy.values()) {
            if (sensorType.getCode().equals(text)) {
                return sensorType;
            }
        }
        throw new IllegalArgumentException("Unknown text:" + text);
    }

    @Override
    public String convertToDatabaseColumn(StatisticValueQueryStrategy sensorType) {
        return convertEnumToString(sensorType);
    }

    @Override
    public StatisticValueQueryStrategy convertToEntityAttribute(String sensorTypeFromDb) {
        return convertStringToEnum(sensorTypeFromDb);
    }

}