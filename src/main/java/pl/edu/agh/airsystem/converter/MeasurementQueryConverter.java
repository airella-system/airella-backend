package pl.edu.agh.airsystem.converter;

import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryRequest;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.converter.SensorTypeConverter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MeasurementQueryConverter {
    public static MeasurementQuery of(MeasurementQueryRequest measurementQueryRequest) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        Duration interval = null;
        List<SensorType> types = null;

        if (measurementQueryRequest.getTimespan() != null) {
            startDate = LocalDateTime.parse(measurementQueryRequest.getTimespan().split("/")[0]);
            endDate = LocalDateTime.parse(measurementQueryRequest.getTimespan().split("/")[1]);
        }
        if (measurementQueryRequest.getInterval() != null) {
            interval = Duration.parse(measurementQueryRequest.getInterval());
        }
        if (measurementQueryRequest.getSensors() != null) {
            types = Stream.of(measurementQueryRequest.getSensors().split(","))
                    .map(SensorTypeConverter::convertStringToEnum)
                    .collect(toList());
        }

        return new MeasurementQuery(startDate, endDate, interval, types);
    }
}
