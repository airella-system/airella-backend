package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.UserClient;

import java.util.Optional;

public interface UserClientRepository extends CrudRepository<UserClient, Long> {
    Optional<UserClient> findByUsername(String username);

    Optional<UserClient> findByStationRegistrationToken(String registerToken);
}