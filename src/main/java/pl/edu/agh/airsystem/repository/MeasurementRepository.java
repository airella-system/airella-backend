package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.Measurement;

import java.util.Optional;

public interface MeasurementRepository extends CrudRepository<Measurement, Long> {
    Optional<Measurement> findById(long id);

}