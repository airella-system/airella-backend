package pl.edu.agh.airsystem.model.api.stations.complexapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.measurement.NewMeasurementRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplexQueryNewMeasurementRequest {
    private String sensorId;
    private NewMeasurementRequest measurement;
}
