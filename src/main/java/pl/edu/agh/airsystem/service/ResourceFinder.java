package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.exception.SensorNotFoundException;
import pl.edu.agh.airsystem.exception.StationNotFoundException;
import pl.edu.agh.airsystem.exception.StatisticDoesNotExistException;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.repository.SensorRepository;
import pl.edu.agh.airsystem.repository.StationRepository;
import pl.edu.agh.airsystem.repository.StatisticRepository;

@Service
@Transactional
@AllArgsConstructor
public class ResourceFinder {
    private final StationRepository stationRepository;
    private final SensorRepository sensorRepository;
    private final StatisticRepository statisticRepository;

    public Station findStation(String stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    public Sensor findSensorInStation(String stationId, String sensorId) {
        return sensorRepository.findByStation_IdAndId(stationId, sensorId)
                .orElseThrow(SensorNotFoundException::new);
    }

    public Statistic findStationStatistic(String stationId, String statisticId) {
        return statisticRepository.findByStation_IdAndId(stationId, statisticId)
                .orElseThrow(StatisticDoesNotExistException::new);
    }

}
