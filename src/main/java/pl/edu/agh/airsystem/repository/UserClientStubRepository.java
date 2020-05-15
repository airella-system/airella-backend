package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.UserClient;
import pl.edu.agh.airsystem.model.database.UserClientStub;

import java.util.Optional;

public interface UserClientStubRepository extends CrudRepository<UserClientStub, Long> {
    Optional<UserClientStub> findByActivateString(String activateString);
    Optional<UserClientStub> findByEmail(String email);
}