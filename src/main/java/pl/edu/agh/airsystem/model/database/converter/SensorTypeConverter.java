package pl.edu.agh.airsystem.model.database.converter;

import pl.edu.agh.airsystem.model.database.SensorType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SensorTypeConverter implements AttributeConverter<SensorType, String> {

    @Override
    public String convertToDatabaseColumn(SensorType sensorType) {
        return sensorType.getCode();
    }

    @Override
    public SensorType convertToEntityAttribute(String sensorTypeFromDb) {
        for (SensorType sensorType : SensorType.values()) {
            if (sensorType.getCode().equals(sensorTypeFromDb)) {
                return sensorType;
            }
        }

        throw new IllegalArgumentException("Unknown database value:" + sensorTypeFromDb);
    }
}