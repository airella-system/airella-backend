package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.airsystem.assembler.StatisticResponseAssembler;
import pl.edu.agh.airsystem.converter.StatisticPrivacyModeConverter;
import pl.edu.agh.airsystem.converter.StatisticTypeConverter;
import pl.edu.agh.airsystem.exception.StatisticAlreadyAddedException;
import pl.edu.agh.airsystem.exception.StatisticDoesNotExistException;
import pl.edu.agh.airsystem.exception.WrongNewStatisticValueType;
import pl.edu.agh.airsystem.model.api.query.StatisticValueQuery;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.response.SuccessResponse;
import pl.edu.agh.airsystem.model.api.sensors.NewSensorResponse;
import pl.edu.agh.airsystem.model.api.statistic.AddStatisticRequest;
import pl.edu.agh.airsystem.model.api.statistic.AddToStatisticRequest;
import pl.edu.agh.airsystem.model.api.statistic.StatisticResponse;
import pl.edu.agh.airsystem.model.api.statistic.StatisticsResponse;
import pl.edu.agh.airsystem.model.database.Client;
import pl.edu.agh.airsystem.model.database.Role;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueEnumStatistic;
import pl.edu.agh.airsystem.model.database.statistic.MultipleValueFloatStatistic;
import pl.edu.agh.airsystem.model.database.statistic.OneValueStringStatistic;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticEnumDefinition;
import pl.edu.agh.airsystem.model.database.statistic.StatisticPrivacyMode;
import pl.edu.agh.airsystem.model.database.statistic.StatisticType;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValueFloat;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValueString;
import pl.edu.agh.airsystem.repository.StationRepository;
import pl.edu.agh.airsystem.repository.StatisticRepository;
import pl.edu.agh.airsystem.repository.StatisticValueRepository;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@AllArgsConstructor
public class StationStatisticService {
    private final StationRepository stationRepository;
    private final StatisticRepository statisticRepository;
    private final StatisticValueRepository statisticValueRepository;
    private final AuthorizationService authorizationService;
    private final ResourceFinder resourceFinder;
    private final StatisticResponseAssembler statisticResponseAssembler;

    public ResponseEntity<Response> addStatistic(String stationId, AddStatisticRequest addStatisticRequest) {
        Client client = authorizationService.checkAuthenticationAndGetClient();
        Station station = resourceFinder.findStation(stationId);

        authorizationService.ensureClientHasStation(client, station);

        statisticRepository.findByStation_IdAndId(stationId, addStatisticRequest.getId())
                .ifPresent((e) -> {
                    throw new StatisticAlreadyAddedException();
                });

        Statistic statistic;
        StatisticType type = StatisticTypeConverter.convertStringToEnum(addStatisticRequest.getType());
        StatisticPrivacyMode privacyMode = StatisticPrivacyModeConverter.convertStringToEnum(addStatisticRequest.getPrivacyMode());

        switch (type) {
            case ONE_STRING:
                statistic = new OneValueStringStatistic(addStatisticRequest.getId(), addStatisticRequest.getName(), station, type, privacyMode);
                break;
            case MULTIPLE_ENUMS:
                statistic = new MultipleValueEnumStatistic(addStatisticRequest.getId(), addStatisticRequest.getName(), addStatisticRequest.getListOfEnumValues(), station, type, privacyMode);
                break;
            case MULTIPLE_FLOATS:
                statistic = new MultipleValueFloatStatistic(addStatisticRequest.getId(), addStatisticRequest.getName(), addStatisticRequest.getMetric(), station, type, privacyMode);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        statistic.setStation(station);
        statisticRepository.save(statistic);
        station.getStatistics().add(statistic);
        stationRepository.save(station);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/stations/{id}/statistics/{id2}")
                .buildAndExpand(station.getId(), statistic.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(DataResponse.of(new NewSensorResponse(statistic.getId())));
    }

    public ResponseEntity<Response> removeStatistic(String stationId, String statisticId) {
        Client client = authorizationService.checkAuthenticationAndGetClient();
        Statistic toDelete = statisticRepository.findByStation_IdAndId(stationId, statisticId)
                .orElseThrow(StatisticDoesNotExistException::new);
        authorizationService.ensureClientHasStatistic(client, toDelete);

        statisticRepository.delete(toDelete);
        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<Response> addToStatistic(String stationId, String statisticId, AddToStatisticRequest addToStatisticRequest) {
        Client client = authorizationService.checkAuthenticationAndGetClient();
        Statistic statistic = statisticRepository.findByStation_IdAndId(stationId, statisticId)
                .orElseThrow(StatisticDoesNotExistException::new);
        authorizationService.ensureClientHasStatistic(client, statistic);

        Object value = addToStatisticRequest.getValue();
        Instant instant = Instant.now();

        StatisticValue statisticValue = null;
        switch (statistic.getStatisticType()) {
            case ONE_STRING: {
                OneValueStringStatistic typedStatistic = (OneValueStringStatistic) statistic;
                if (!(value instanceof String)) {
                    throw new WrongNewStatisticValueType();
                }
                statisticValue = new StatisticValueString(statistic, instant, (String) value);
                typedStatistic.setValue((StatisticValueString) statisticValue);
            }
            break;
            case MULTIPLE_ENUMS: {
                MultipleValueEnumStatistic typedStatistic = (MultipleValueEnumStatistic) statistic;
                if (!(value instanceof String) ||
                        !(typedStatistic.getStatisticEnumDefinitions().stream().map(StatisticEnumDefinition::getId).collect(toList())).contains(value)) {
                    throw new WrongNewStatisticValueType();
                }
                statisticValue = new StatisticValueString(statistic, instant, (String) value);
                typedStatistic.getValues().add((StatisticValueString) statisticValue);
                typedStatistic.setLatestStatisticValue((StatisticValueString) statisticValue);
            }
            break;
            case MULTIPLE_FLOATS: {
                MultipleValueFloatStatistic typedStatistic = (MultipleValueFloatStatistic) statistic;
                if (!(value instanceof Double)) {
                    throw new WrongNewStatisticValueType();
                }
                statisticValue = new StatisticValueFloat(statistic, instant, (Double) value);
                typedStatistic.getValues().add((StatisticValueFloat) statisticValue);
                typedStatistic.setLatestStatisticValue((StatisticValueFloat) statisticValue);
            }
            break;
        }

        statisticValueRepository.save(statisticValue);
        statisticRepository.save(statistic);
        return ResponseEntity.ok(new SuccessResponse());
    }

    public ResponseEntity<Response> getStatistic(String stationId, String statisticId, StatisticValueQuery statisticQuery) {
        Statistic statistic = resourceFinder.findStationStatistic(stationId, statisticId);

        if (statistic.getStatisticPrivacyMode() == StatisticPrivacyMode.PRIVATE) {
            Client client = authorizationService.checkAuthenticationAndGetClient();
            authorizationService.ensureClientHasStatistic(client, statistic);
        }

        StatisticResponse statisticResponse = statisticResponseAssembler.assemble(statistic, statisticQuery);
        return ResponseEntity.ok(DataResponse.of(statisticResponse));
    }

    public ResponseEntity<Response> getStatistics(String stationId, StatisticValueQuery statisticQuery) {
        Station station = resourceFinder.findStation(stationId);
        Client client = authorizationService.getClient();

        List<StatisticResponse> statisticResponses = station.getStatistics().stream()
                .filter(statistic -> checkIfStatisticIsVisible(client, statistic))
                .map(statistic -> statisticResponseAssembler.assemble(statistic, statisticQuery))
                .collect(toList());

        return ResponseEntity.ok(DataResponse.of(new StatisticsResponse(statisticResponses)));

    }

    private boolean checkIfStatisticIsVisible(Client client, Statistic statistic) {
        if (client != null && client.getRoles().contains(Role.ROLE_ADMIN)) return true;
        if (statistic.getStatisticPrivacyMode() == StatisticPrivacyMode.PUBLIC) {
            return true;
        } else {
            return client != null && authorizationService.checkIfClientHasStatistic(client, statistic);
        }
    }

}
