package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.Station;

public interface StationRepository extends CrudRepository<Station, Long> {
}