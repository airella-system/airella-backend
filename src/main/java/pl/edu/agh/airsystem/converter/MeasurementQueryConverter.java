package pl.edu.agh.airsystem.converter;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryRequest;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy;
import pl.edu.agh.airsystem.model.database.SensorType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.AVG;

@Component
public class MeasurementQueryConverter implements Converter<MeasurementQueryRequest, MeasurementQuery> {

    @Override
    public MeasurementQuery convert(MeasurementQueryRequest measurementQueryRequest) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        Duration interval = null;
        List<SensorType> types = null;
        MeasurementQueryStrategy strategy = AVG;
        boolean interpolate = true;

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

        if (measurementQueryRequest.getStrategy() != null) {
            strategy = MeasurementQueryStrategyConverter.
                    convertStringToEnum(measurementQueryRequest.getStrategy());
        }

        if (measurementQueryRequest.getInterpolate() != null) {
            interpolate = Boolean.parseBoolean(measurementQueryRequest.getInterpolate());
        }

        return new MeasurementQuery(startDate, endDate, interval,
                types, strategy, interpolate);
    }
}
