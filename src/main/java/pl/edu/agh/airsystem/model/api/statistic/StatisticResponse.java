package pl.edu.agh.airsystem.model.api.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatisticResponse {
    private String id;
    private String name;
    private String type;
    private String privacyMode;
    private final List<? extends StatisticValueResponse> values;
}
