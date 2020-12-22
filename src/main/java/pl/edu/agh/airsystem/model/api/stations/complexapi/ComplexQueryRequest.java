package pl.edu.agh.airsystem.model.api.stations.complexapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplexQueryRequest {
    private ComplexQueryAddRequest add;
    private ComplexQuerySetRequest set;
}
