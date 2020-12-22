package pl.edu.agh.airsystem.model.api.stations.complexapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.measurement.NewMeasurementRequest;
import pl.edu.agh.airsystem.model.api.statistic.AddToStatisticRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplexQueryNewStatisticValueRequest {
    private String statisticId;
    private AddToStatisticRequest statisticValue;
}
