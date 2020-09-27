package pl.edu.agh.airsystem.converter;

import pl.edu.agh.airsystem.model.database.statistic.StatisticChartType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatisticChartTypeConverter implements AttributeConverter<StatisticChartType, String> {

    public static String convertEnumToString(StatisticChartType statisticType) {
        return statisticType.getCode();
    }

    public static StatisticChartType convertStringToEnum(String statisticChartTypeFromDb) {
        for (StatisticChartType statisticChartType : StatisticChartType.values()) {
            if (statisticChartType.getCode().equals(statisticChartTypeFromDb)) {
                return statisticChartType;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + statisticChartTypeFromDb);
    }

    @Override
    public String convertToDatabaseColumn(StatisticChartType statisticChartType) {
        return convertEnumToString(statisticChartType);
    }

    @Override
    public StatisticChartType convertToEntityAttribute(String statisticChartTypeFromDb) {
        return convertStringToEnum(statisticChartTypeFromDb);
    }

}
