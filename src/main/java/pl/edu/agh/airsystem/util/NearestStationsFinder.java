package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static pl.edu.agh.airsystem.util.GeographicUtils.distance;

@Component
@Transactional
@AllArgsConstructor
public class NearestStationsFinder {
    private StationRepository stationRepository;

    public List<NearStation> findNearestSensors(Location location, int n, float maxMeters) {
        List<Station> stations = new ArrayList<>();
        stationRepository.findAll().forEach(stations::add);

        return stations.stream()
                .map(station -> new NearStation(station, distance(location, station.getLocation())))
                .filter(nearStation -> nearStation.getDistance() < maxMeters)
                .sorted(comparing(NearStation::getDistance))
                .limit(n)
                .collect(toList());
    }
}
