package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.database.UserClient;
import pl.edu.agh.airsystem.repository.UserClientRepository;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserClientRepository userClientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserClient userClient = userClientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(userClient.getEmail(), userClient.getPasswordHash(), emptyList());
    }

}
