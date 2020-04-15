package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.stations.StationResponse;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.edu.agh.airsystem.util.GeographicUtils.distance;

@Service
@AllArgsConstructor
public class SearchService {
    private final StationRepository stationRepository;

    public ResponseEntity<List<StationResponse>> getStations(
            double latitude, double longitude, double radius,
            MeasurementQuery measurementQuery) {
        List<Station> stations = new ArrayList<>();
        stationRepository.findAll().forEach(stations::add);

        Location location = new Location(latitude, longitude);

        List<StationResponse> response = stations.stream()
                .filter(e -> e.getLocation() != null)
                .filter(e -> distance(location, e.getLocation()) < radius)
                .map(e -> new StationResponse(e, measurementQuery))
                .collect(toList());

        return ResponseEntity.ok().body(response);
    }

}