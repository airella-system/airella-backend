package pl.edu.agh.airsystem.model.api.stations.complexapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorRequest;
import pl.edu.agh.airsystem.model.api.statistic.AddStatisticRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplexQueryAddRequest {
    private List<NewSensorRequest> sensors;
    private List<AddStatisticRequest> statistics;
    private List<ComplexQueryNewMeasurementRequest> measurements;
    private List<ComplexQueryNewStatisticValueRequest> statisticValues;
}
