package pl.edu.agh.airsystem.service.measurement.queryinvoker;

import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.sensors.MeasurementResponse;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.util.List;

public interface MeasurementQueryInvoker {
    List<? extends MeasurementResponse> apply(Sensor sensor, MeasurementQuery measurementQuery);

    boolean isApplicable(MeasurementQuery measurementQuery);
}
