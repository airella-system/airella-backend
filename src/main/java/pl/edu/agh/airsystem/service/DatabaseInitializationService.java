package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.UserClient;
import pl.edu.agh.airsystem.repository.StationClientRepository;
import pl.edu.agh.airsystem.repository.StationRepository;
import pl.edu.agh.airsystem.repository.UserClientRepository;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class DatabaseInitializationService {
    private final UserClientRepository userClientRepository;
    private final StationClientRepository stationClientRepository;
    private final StationRepository stationRepository;

    @PostConstruct
    private void postConstruct() {
        //create default user if not exists
        if (userClientRepository.findByUsername("admin").isEmpty()) {
            UserClient admin = new UserClient("admin",
                    new BCryptPasswordEncoder().encode("admin"));
            userClientRepository.save(admin);
        }
    }

}
