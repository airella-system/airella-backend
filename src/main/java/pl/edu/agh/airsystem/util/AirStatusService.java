package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;
import pl.edu.agh.airsystem.model.database.Station;

@Service
@AllArgsConstructor
public class AirStatusService {
    private SensorUtilsService sensorUtilsService;

    //TODO add calculation method
    public Double calculateSensorStatus(Sensor sensor) {
        Double sensorLastValue = sensorUtilsService.findLatestMeasurementInSensor(sensor)
                .map(Measurement::getValue)
                .orElse(null);
        if (sensorLastValue == null) {
            return null;
        } else {
            return 50.0;
        }
    }

    //TODO add calculation method
    public Double calculateAirQualityIndex(Station station) {
        return station.getSensors().stream()
                .filter(e -> e.getType() == SensorType.PM10)
                .findFirst()
                .flatMap(sensor -> sensorUtilsService.findLatestMeasurementInSensor(sensor)
                        .map(Measurement::getValue))
                .orElse(null);
    }

}
