package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.airsystem.model.database.Station;

@Getter
@AllArgsConstructor
public class NearStation {
    private Station station;
    private double distance;
}
