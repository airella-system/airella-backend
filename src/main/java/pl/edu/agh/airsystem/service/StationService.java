package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.assembler.BriefStationResponseAssembler;
import pl.edu.agh.airsystem.assembler.StationResponseAssembler;
import pl.edu.agh.airsystem.exception.NotUsersStationException;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.stations.*;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.StationClient;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    private final ResourceFinder resourceFinder;
    private final AuthorizationService authorizationService;
    private final StationResponseAssembler stationResponseAssembler;
    private final BriefStationResponseAssembler briefStationResponseAssembler;

    public ResponseEntity<List<BriefStationResponse>> getStations() {
        List<BriefStationResponse> response = new ArrayList<>();
        stationRepository.findAll().forEach(station -> response.add(briefStationResponseAssembler.assemble(station)));
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<StationResponse> getStation(Long stationId, MeasurementQuery measurementQuery) {
        Station station = resourceFinder.findStation(stationId);
        StationResponse stationResponse = stationResponseAssembler.assemble(station, measurementQuery);
        return ResponseEntity.ok().body(stationResponse);
    }

    public void ensureSelectedStationAuthorization(Station selectedStation) {
        StationClient loggedStation = authorizationService.checkAuthenticationAndGetStationClient();
        if (selectedStation.getStationClient().getId() != loggedStation.getId()) {
            throw new NotUsersStationException();
        }
    }

    public ResponseEntity<?> setStationName(
            Long stationId,
            NameChangeRequest nameChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        ensureSelectedStationAuthorization(station);

        station.setName(nameChangeRequest.getName());
        stationRepository.save(station);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> setStationAddress(
            Long stationId,
            AddressChangeRequest addressChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        ensureSelectedStationAuthorization(station);

        Address address = new Address(
                addressChangeRequest.getCountry(),
                addressChangeRequest.getCity(),
                addressChangeRequest.getStreet(),
                addressChangeRequest.getNumber()
        );

        station.setAddress(address);
        stationRepository.save(station);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> setStationLocation(
            Long stationId,
            LocationChangeRequest locationChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        ensureSelectedStationAuthorization(station);

        Location location = new Location(
                locationChangeRequest.getLatitude(),
                locationChangeRequest.getLongitude());

        station.setLocation(location);
        stationRepository.save(station);

        return ResponseEntity.ok().build();
    }
}

