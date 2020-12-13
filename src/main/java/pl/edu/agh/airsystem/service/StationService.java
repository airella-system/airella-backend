package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.assembler.BriefStationResponseAssembler;
import pl.edu.agh.airsystem.assembler.StationResponseAssembler;
import pl.edu.agh.airsystem.model.api.query.MeasurementQuery;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.response.SuccessResponse;
import pl.edu.agh.airsystem.model.api.stations.AddressChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.BriefStationResponse;
import pl.edu.agh.airsystem.model.api.stations.LocationChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.NameChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.StationResponse;
import pl.edu.agh.airsystem.model.api.stations.complexapi.ComplexQueryAddRequest;
import pl.edu.agh.airsystem.model.api.stations.complexapi.ComplexQueryRequest;
import pl.edu.agh.airsystem.model.database.*;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueEnumStatistic;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueFloatStatistic;
import pl.edu.agh.airsystem.model.database.statistic.OneValueStringStatistic;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class StationService {
    private final StatisticRepository statisticRepository;
    private final SensorRepository sensorRepository;
    private final StationRepository stationRepository;
    private final MeasurementRepository measurementRepository;
    private final StatisticValueRepository statisticValueRepository;
    private final AddressRepository addressRepository;
    private final ResourceFinder resourceFinder;
    private final AuthorizationService authorizationService;
    private final StationResponseAssembler stationResponseAssembler;
    private final BriefStationResponseAssembler briefStationResponseAssembler;

    private final SensorService sensorService;
    private final MeasurementService measurementService;
    private final StationStatisticService stationStatisticService;


    public ResponseEntity<Response> getStations() {
        List<BriefStationResponse> response = new ArrayList<>();
        stationRepository.findAll().forEach(station -> response.add(briefStationResponseAssembler.assemble(station)));
        return ResponseEntity.ok().body(DataResponse.of(response));
    }

    public ResponseEntity<Response> getStation(String stationId, MeasurementQuery measurementQuery) {
        Station station = resourceFinder.findStation(stationId);
        StationResponse stationResponse = stationResponseAssembler.assemble(station, measurementQuery);
        return ResponseEntity.ok(DataResponse.of(stationResponse));
    }

    @Transactional
    public ResponseEntity<Response> deleteStation(String stationId) {
        Station station = resourceFinder.findStation(stationId);
        Client client = authorizationService.checkAuthenticationAndGetClient();
        authorizationService.ensureClientHasStation(client, station);

        Set<Long> sensorsDbIds = station.getSensors().stream()
                .map(Sensor::getDbId)
                .collect(Collectors.toSet());

        Set<Long> statisticsDbIds = station.getStatistics().stream()
                .map(Statistic::getDbId)
                .collect(Collectors.toSet());

        station.getSensors().forEach(sensor -> {
            sensor.setLatestMeasurement(null);
            sensorRepository.saveAndFlush(sensor);
        });

        station.getStatistics().forEach(statistic -> {
            if (statistic instanceof OneValueStringStatistic) {
                ((OneValueStringStatistic) statistic).setValue(null);
            } else if (statistic instanceof MultipleValueFloatStatistic) {
                ((MultipleValueFloatStatistic) statistic).setLatestStatisticValue(null);
            } else if (statistic instanceof MultipleValueEnumStatistic) {
                ((MultipleValueEnumStatistic) statistic).setLatestStatisticValue(null);
            }
            statisticRepository.saveAndFlush(statistic);
        });

        measurementRepository.deleteAllMeasurementsForSelectedSensors(sensorsDbIds);
        statisticValueRepository.deleteAllMeasurementsForSelectedStatistics(statisticsDbIds);
        stationRepository.delete(station);
        return ResponseEntity.ok(DataResponse.of(new SuccessResponse()));
    }

    public ResponseEntity<Response> setStationName(
            String stationId,
            NameChangeRequest nameChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        authorizationService.ensureSelectedStationAuthorization(station);

        station.setName(nameChangeRequest.getName());
        stationRepository.save(station);

        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<Response> setStationAddress(
            String stationId,
            AddressChangeRequest addressChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        authorizationService.ensureSelectedStationAuthorization(station);

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
            String stationId,
            LocationChangeRequest locationChangeRequest) {
        Station station = resourceFinder.findStation(stationId);
        authorizationService.ensureSelectedStationAuthorization(station);

        Location location = new Location(
                locationChangeRequest.getLatitude(),
                locationChangeRequest.getLongitude());

        station.setLocation(location);
        stationRepository.save(station);

        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<? extends Response> getCurrentUserStations() {
        UserClient userClient = authorizationService.checkAuthenticationAndGetUserClient();
        return getUserStations(userClient);
    }

    public ResponseEntity<Response> getUserStations(UserClient userClient) {
        List<BriefStationResponse> response = new ArrayList<>();
        if (userClient.getRoles().contains(Role.ROLE_ADMIN)) {
            stationRepository.findAll().forEach(station -> response.add(briefStationResponseAssembler.assemble(station)));
        } else {
            stationRepository.findByOwner(userClient).forEach(station -> response.add(briefStationResponseAssembler.assemble(station)));
        }
        return ResponseEntity.ok().body(DataResponse.of(response));
    }

    public ResponseEntity<? extends Response> makeComplexQuery(String stationId, ComplexQueryRequest complexQueryRequest) {
        Station station = resourceFinder.findStation(stationId);
        //authorizationService.ensureSelectedStationAuthorization(station);

        Optional.ofNullable(complexQueryRequest.getAdd())
                .flatMap(addRequest -> Optional.ofNullable(addRequest.getSensors()))
                .ifPresent(newSensorRequestsList ->
                        sensorService.addSensors(stationId, newSensorRequestsList));

        Optional.ofNullable(complexQueryRequest.getAdd())
                .flatMap(addRequest -> Optional.ofNullable(addRequest.getStatistics()))
                .ifPresent(newStatisticRequestsList ->
                        stationStatisticService.addStatistics(stationId, newStatisticRequestsList));

        Optional.ofNullable(complexQueryRequest.getAdd())
                .flatMap(addRequest -> Optional.ofNullable(addRequest.getMeasurements()))
                .ifPresent(newMeasurementRequestsList ->
                        measurementService.addMeasurements(stationId, newMeasurementRequestsList));

        Optional.ofNullable(complexQueryRequest.getAdd())
                .flatMap(addRequest -> Optional.ofNullable(addRequest.getStatisticValues()))
                .ifPresent(newStatisticValueRequestList ->
                        stationStatisticService.addToStatistic(stationId, newStatisticValueRequestList));

        Optional.ofNullable(complexQueryRequest.getSet())
                .flatMap(setRequest -> Optional.ofNullable(setRequest.getName()))
                .ifPresent(setNameRequest ->
                        setStationName(stationId, setNameRequest));

        Optional.ofNullable(complexQueryRequest.getSet())
                .flatMap(setRequest -> Optional.ofNullable(setRequest.getAddress()))
                .ifPresent(setAddressRequest ->
                        setStationAddress(stationId, setAddressRequest));

        Optional.ofNullable(complexQueryRequest.getSet())
                .flatMap(setRequest -> Optional.ofNullable(setRequest.getLocation()))
                .ifPresent(setLocationRequest ->
                        setStationLocation(stationId, setLocationRequest));

        return ResponseEntity.ok(new SuccessResponse());
    }
}
