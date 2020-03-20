package pl.edu.agh.airsystem.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.ApplicationUser;
import pl.edu.agh.airsystem.model.Role;
import pl.edu.agh.airsystem.repository.UserRepository;

import javax.annotation.PostConstruct;

@Service
public class DatabaseInit {
    private final UserRepository userRepository;

    public DatabaseInit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void postConstruct() {
        //create default user if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            ApplicationUser admin = new ApplicationUser("admin",
                    new BCryptPasswordEncoder().encode("admin"));
            admin.addRole(Role.ROLE_USER);
            userRepository.save(admin);
        }
    }
}
