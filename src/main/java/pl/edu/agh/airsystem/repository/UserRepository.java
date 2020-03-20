package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.ApplicationUser;

import java.util.Optional;

public interface UserRepository extends CrudRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);
}