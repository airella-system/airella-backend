package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.exception.SensorNotFoundException;
import pl.edu.agh.airsystem.exception.StationNotFoundException;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.repository.StationRepository;

@Service
@AllArgsConstructor
public class ResourceFinder {
    private StationRepository stationRepository;

    public Station findStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    public Sensor findSensorInStation(long stationId, String sensorId) {
        Station station = findStation(stationId);
        return station.getSensors().stream()
                .filter(e -> e.getId().equals(sensorId))
                .findFirst()
                .orElseThrow(SensorNotFoundException::new);
    }

}
