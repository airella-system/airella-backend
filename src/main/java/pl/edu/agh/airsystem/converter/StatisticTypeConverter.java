package pl.edu.agh.airsystem.converter;

import pl.edu.agh.airsystem.model.database.statistic.StatisticType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatisticTypeConverter implements AttributeConverter<StatisticType, String> {

    public static String convertEnumToString(StatisticType statisticType) {
        return statisticType.getCode();
    }

    public static StatisticType convertStringToEnum(String statisticTypeFromDb) {
        for (StatisticType sensorType : StatisticType.values()) {
            if (sensorType.getCode().equals(statisticTypeFromDb)) {
                return sensorType;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + statisticTypeFromDb);
    }

    @Override
    public String convertToDatabaseColumn(StatisticType statisticType) {
        return convertEnumToString(statisticType);
    }

    @Override
    public StatisticType convertToEntityAttribute(String statisticTypeFromDb) {
        return convertStringToEnum(statisticTypeFromDb);
    }

}