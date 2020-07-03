package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.assembler.BriefStationResponseAssembler;
import pl.edu.agh.airsystem.assembler.StationResponseAssembler;
import pl.edu.agh.airsystem.exception.NotUsersStationException;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.response.SuccessResponse;
import pl.edu.agh.airsystem.model.api.stations.*;
import pl.edu.agh.airsystem.model.database.*;
import pl.edu.agh.airsystem.repository.AddressRepository;
import pl.edu.agh.airsystem.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    private final AddressRepository addressRepository;
    private final ResourceFinder resourceFinder;
    private final AuthorizationService authorizationService;
    private final StationResponseAssembler stationResponseAssembler;
    private final BriefStationResponseAssembler briefStationResponseAssembler;

    public ResponseEntity<Response> getStations() {
        List<BriefStationResponse> response = new ArrayList<>();
        stationRepository.findAll().forEach(station -> response.add(briefStationResponseAssembler.assemble(station)));
        return ResponseEntity.ok().body(DataResponse.of(response));
    }

    public ResponseEntity<Response> getStation(Long stationId, MeasurementQuery measurementQuery) {
        Station station = resourceFinder.findStation(stationId);
        StationResponse stationResponse = stationResponseAssembler.assemble(station, measurementQuery);
        return ResponseEntity.ok(DataResponse.of(stationResponse));
    }

    public ResponseEntity<Response> deleteStation(Long stationId) {
        Station station = resourceFinder.findStation(stationId);
        Client client = authorizationService.checkAuthenticationAndGetClient();

        if (client instanceof UserClient && client.getId() != station.getOwner().getId()
                || client instanceof StationClient && client.getId() != station.getStationClient().getId()) {
            throw new NotUsersStationException();
        }
        stationRepository.delete(station);
        return ResponseEntity.ok(DataResponse.of(new SuccessResponse()));
    }

    public void ensureSelectedStationAuthorization(Station selectedStation) {
        StationClient loggedStation = authorizationService.checkAuthenticationAndGetStationClient();
        if (selectedStation.getStationClient().getId() != loggedStation.getId()) {
            throw new NotUsersStationException();
        }
    }

    public ResponseEntity<Response> setStationName(
            Long stationId,
            NameChangeRequest nameChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        ensureSelectedStationAuthorization(station);

        station.setName(nameChangeRequest.getName());
        stationRepository.save(station);

        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<Response> setStationAddress(
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
        addressRepository.save(address);

        station.setAddress(address);
        stationRepository.save(station);

        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<Response> setStationLocation(
            Long stationId,
            LocationChangeRequest locationChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        ensureSelectedStationAuthorization(station);

        Location location = new Location(
                locationChangeRequest.getLatitude(),
                locationChangeRequest.getLongitude());

        station.setLocation(location);
        stationRepository.save(station);

        return ResponseEntity.ok(new SuccessResponse());
    }
}

