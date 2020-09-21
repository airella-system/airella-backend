package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.assembler.VirtualStationResponseAssembler;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.util.InverseDistanceWeighting;
import pl.edu.agh.airsystem.util.NearStation;
import pl.edu.agh.airsystem.util.NearestStationsFinder;
import pl.edu.agh.airsystem.util.SensorUtilsService;
import pl.edu.agh.airsystem.util.StationUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@AllArgsConstructor
public class SensorInterpolationService {
    private SensorUtilsService sensorUtilsService;
    private NearestStationsFinder nearestStationsFinder;
    private VirtualStationResponseAssembler virtualStationResponseAssembler;

    public ResponseEntity<DataResponse> getResponse(double latitude, double longitude) {
        Location location = new Location(latitude, longitude);
        List<NearStation> nearestStations = nearestStationsFinder.findNearestSensors(location, 5, 1000);

        List<SensorType> sensorTypes = List.of(
                SensorType.PM1,
                SensorType.PM10,
                SensorType.PM2_5,
                SensorType.TEMP,
                SensorType.HUMIDITY);

        Station virtualStation = new Station();
        List<Sensor> virtualSensors = new ArrayList<>();

        for (SensorType sensorType : sensorTypes) {
            Sensor virtualSensor = new Sensor();
            virtualSensors.add(virtualSensor);
            virtualSensor.setType(sensorType);
            virtualSensor.setMeasurements(new HashSet<>());

            List<NearMeasurement> nearMeasurements = nearestStations.stream()
                    .map(station -> StationUtils.getSensorById(station.getStation(), sensorType.getCode())
                            .flatMap(sensorUtilsService::findLatestMeasurementInSensor)
                            .map(measurement -> new NearMeasurement(station.getDistance(), measurement)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());

            if (nearMeasurements.size() >= 2) {
                Double value = InverseDistanceWeighting.apply(nearMeasurements,
                        nearMeasurement -> nearMeasurement.distance,
                        nearMeasurement -> nearMeasurement.measurement.getValue());
                if (value != null) {
                    Measurement measurement = new Measurement();
                    measurement.setValue(value);
                    virtualSensor.getMeasurements().add(measurement);
                }
            }
        }

        virtualStation.setSensors(virtualSensors);

        return ResponseEntity.ok(DataResponse.of(
                virtualStationResponseAssembler.assemble(virtualStation)));
    }

    @AllArgsConstructor
    private static class NearMeasurement {
        double distance;
        Measurement measurement;
    }

}
