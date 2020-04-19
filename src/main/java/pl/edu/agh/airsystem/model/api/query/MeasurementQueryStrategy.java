package pl.edu.agh.airsystem.model.api.query;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MeasurementQueryStrategy {
    AVG("avg"), LATEST("latest"), ALL("all");

    private String code;

    public String getCode() {
        return code;
    }
}
