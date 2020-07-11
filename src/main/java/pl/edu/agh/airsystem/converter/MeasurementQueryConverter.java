package pl.edu.agh.airsystem.converter;

import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryRequest;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.util.Timespan;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static pl.edu.agh.airsystem.model.api.query.MeasurementQueryStrategy.LATEST;

@Component
public class MeasurementQueryConverter implements Converter<MeasurementQueryRequest, MeasurementQuery> {

    @Override
    public MeasurementQuery convert(MeasurementQueryRequest measurementQueryRequest) {
        Timespan timespan = null;
        Duration interval = null;
        List<SensorType> types = null;
        MeasurementQueryStrategy strategy = LATEST;
        boolean interpolate = true;

        if (measurementQueryRequest.getTimespan() != null) {
            Instant startDate = Instant.parse(measurementQueryRequest.getTimespan().split("/")[0]);
            Instant endDate = Instant.parse(measurementQueryRequest.getTimespan().split("/")[1]);
            timespan = new Timespan(startDate, endDate);
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

        return new MeasurementQuery(timespan, interval,
                types, strategy, interpolate);
    }
}
