package pl.edu.agh.airsystem.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.Role;
import pl.edu.agh.airsystem.model.database.UserClient;
import pl.edu.agh.airsystem.repository.UserClientRepository;

import javax.annotation.PostConstruct;

@Service
@Transactional
@NoArgsConstructor
public class DatabaseInitializationService {
    private UserClientRepository userClientRepository;

    @Value("${airella.admin.special.enabled}")
    private boolean adminEnabled;

    @Value("${airella.admin.special.username}")
    private String adminUsername;

    @Value("${airella.admin.special.password}")
    private String adminPassword;

    public DatabaseInitializationService(UserClientRepository userClientRepository) {
        this.userClientRepository = userClientRepository;
    }

    @PostConstruct
    private void postConstruct() {
        if (adminEnabled) {
            //create default user if not exists
            if (userClientRepository.findByEmail(adminUsername).isEmpty()) {
                UserClient admin = new UserClient(adminUsername,
                        new BCryptPasswordEncoder().encode(adminPassword));
                admin.getRoles().add(Role.ROLE_USER);
                admin.getRoles().add(Role.ROLE_ADMIN);
                userClientRepository.save(admin);
            }
        }
    }

}
