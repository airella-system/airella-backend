package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StationVisibility {
    PUBLIC("public"), PRIVATE("private");

    private final String code;
}
