package pl.edu.agh.airsystem.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.Measurement;
import pl.edu.agh.airsystem.model.database.Sensor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeasurementRepository extends CrudRepository<Measurement, Long> {
    Optional<Measurement> findById(long id);

    List<Measurement> findBySensorAndTimestampAfterAndTimestampBefore(Sensor sensor, Instant after, Instant before);

    Optional<Measurement> findFirstBySensorAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(Sensor sensor, Instant after, Instant before);

    Optional<Measurement> findFirstBySensorAndTimestampBeforeOrderByTimestampDesc(Sensor sensor, Instant before);

    @Query("SELECT AVG(m.value) FROM Measurement m WHERE m.sensor=(:sensor) AND m.timestamp < (:before) AND m.timestamp > (:after)")
    Double findAverageBySensorAndTimestampAfterAndTimestampBefore(@Param("sensor") Sensor sensor, @Param("after") Instant after, @Param("before") Instant before);

    @Transactional
    @Modifying
    @Query("DELETE FROM Measurement m WHERE m.sensor.dbId IN (:ids)")
    void deleteAllMeasurementsForSelectedSensors(@Param("ids") Set<Long> ids);
}