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
import java.util.Optional;

@Service
@Transactional
public class DatabaseInitializationService {
    private final UserClientRepository userClientRepository;

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
        Optional<UserClient> optionalAdmin = userClientRepository.findByRolesContaining(Role.ROLE_START_ADMIN);
        if (adminEnabled) {
            UserClient admin;
            if (optionalAdmin.isEmpty()) {
                admin = new UserClient(adminUsername,
                        new BCryptPasswordEncoder().encode(adminPassword));
            } else {
                admin = optionalAdmin.get();
                admin.setEmail(adminUsername);
                admin.setPasswordHash(new BCryptPasswordEncoder().encode(adminPassword));
            }
            admin.getRoles().add(Role.ROLE_USER);
            admin.getRoles().add(Role.ROLE_ADMIN);
            admin.getRoles().add(Role.ROLE_START_ADMIN);
            userClientRepository.save(admin);
        } else {
            optionalAdmin.ifPresent(userClientRepository::delete);
        }
    }

}
