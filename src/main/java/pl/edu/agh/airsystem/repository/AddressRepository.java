package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Client;

import java.util.Optional;

public interface AddressRepository extends CrudRepository<Address, Long> {
}