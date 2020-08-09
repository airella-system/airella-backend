package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.Station;
import pl.edu.agh.airsystem.model.database.UserClient;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends CrudRepository<Station, Long> {
    Optional<Station> findById(String s);

    List<Station> findByName(String s);
    List<Station> findByOwner(UserClient userClient);
}