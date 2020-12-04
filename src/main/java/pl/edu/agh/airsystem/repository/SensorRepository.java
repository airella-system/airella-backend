package pl.edu.agh.airsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.Sensor;
import pl.edu.agh.airsystem.model.database.SensorType;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findByStation_IdAndId(String stationId, String sensorId);

    List<Sensor> findByStation_IdAndTypeIn(String stationId, List<SensorType> sensorTypes);
}