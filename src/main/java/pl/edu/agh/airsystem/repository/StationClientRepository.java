package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.StationClient;

public interface StationClientRepository extends CrudRepository<StationClient, Long> {
}