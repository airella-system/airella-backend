package pl.edu.agh.airsystem.converter;

import pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MeasurementQueryStrategyConverter implements AttributeConverter<MeasurementQueryStrategy, String> {

    public static String convertEnumToString(MeasurementQueryStrategy sensorType) {
        return sensorType.getCode();
    }

    public static MeasurementQueryStrategy convertStringToEnum(String text) {
        for (MeasurementQueryStrategy sensorType : MeasurementQueryStrategy.values()) {
            if (sensorType.getCode().equals(text)) {
                return sensorType;
            }
        }
        throw new IllegalArgumentException("Unknown text:" + text);
    }

    @Override
    public String convertToDatabaseColumn(MeasurementQueryStrategy sensorType) {
        return convertEnumToString(sensorType);
    }

    @Override
    public MeasurementQueryStrategy convertToEntityAttribute(String sensorTypeFromDb) {
        return convertStringToEnum(sensorTypeFromDb);
    }

}