package pl.edu.agh.airsystem.converter;

import pl.edu.agh.airsystem.model.database.StationVisibility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StationVisibilityConverter implements AttributeConverter<StationVisibility, String> {

    public static String convertEnumToString(StationVisibility sensorType) {
        return sensorType.getCode();
    }

    public static StationVisibility convertStringToEnum(String stationVisibilityFromDb) {
        for (StationVisibility stationVisibility : StationVisibility.values()) {
            if (stationVisibility.getCode().equals(stationVisibilityFromDb)) {
                return stationVisibility;
            }
        }
        throw new IllegalArgumentException("Unknown database value:" + stationVisibilityFromDb);
    }

    @Override
    public String convertToDatabaseColumn(StationVisibility stationVisibility) {
        return convertEnumToString(stationVisibility);
    }

    @Override
    public StationVisibility convertToEntityAttribute(String stationVisibilityFromDb) {
        return convertStringToEnum(stationVisibilityFromDb);
    }

}