package pl.edu.agh.airsystem.util;

import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;

import java.util.Optional;

@Transactional
public class StationUtils {

    public static Optional<Sensor> getSensorById(Station station, String id) {
        return station.getSensors().stream()
                .filter(sensor -> sensor.getId().equals(id))
                .findFirst();
    }

}
