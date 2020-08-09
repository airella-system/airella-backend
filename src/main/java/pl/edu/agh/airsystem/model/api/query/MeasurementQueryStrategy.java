package pl.edu.agh.airsystem.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeasurementQueryStrategy {
    AVG("avg"), LATEST("latest"), ALL("all");

    private String code;
}
