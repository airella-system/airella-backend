package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.Sensor;

public interface SensorRepository extends CrudRepository<Sensor, Long> {
}