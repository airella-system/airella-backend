package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.UserClient;
import pl.edu.agh.airsystem.repository.StationClientRepository;
import pl.edu.agh.airsystem.repository.StationRepository;
import pl.edu.agh.airsystem.repository.UserClientRepository;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

@Service
@AllArgsConstructor
public class DatabaseInitializationService {
    private final UserClientRepository userClientRepository;
    private final StationClientRepository stationClientRepository;
    private final StationRepository stationRepository;
    private final EmailSenderService essa;

    @PostConstruct
    private void postConstruct() {
        //create default user if not exists

        try {
            essa.sendActivationString("dupa", "cyc");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        if (userClientRepository.findByEmail("admin@gmail.com").isEmpty()) {
            UserClient admin = new UserClient("admin@gmail.com",
                    new BCryptPasswordEncoder().encode("admin"));
            userClientRepository.save(admin);
        }
    }

}
