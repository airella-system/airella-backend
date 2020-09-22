package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.Role;
import pl.edu.agh.airsystem.model.database.UserClient;
import pl.edu.agh.airsystem.repository.UserClientRepository;

import javax.annotation.PostConstruct;

@Service
@Transactional
@AllArgsConstructor
public class DatabaseInitializationService {
    private final UserClientRepository userClientRepository;

    @PostConstruct
    private void postConstruct() {
        //create default user if not exists
        if (userClientRepository.findByEmail("admin@gmail.com").isEmpty()) {
            UserClient admin = new UserClient("admin@gmail.com",
                    new BCryptPasswordEncoder().encode("admin"));
            admin.getRoles().add(Role.ROLE_USER);
            admin.getRoles().add(Role.ROLE_ADMIN);
            userClientRepository.save(admin);
        }
    }

}
