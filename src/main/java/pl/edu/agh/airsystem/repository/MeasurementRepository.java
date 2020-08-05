package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends CrudRepository<Measurement, Long> {
    Optional<Measurement> findById(long id);

    List<Measurement> findBySensorAndTimestampAfterAndTimestampBefore(Sensor sensor, Instant after, Instant before);

    Optional<Measurement> findFirstBySensorAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(Sensor sensor, Instant after, Instant before);

}