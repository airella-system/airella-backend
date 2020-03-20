package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.model.ApplicationUser;
import pl.edu.agh.airsystem.repository.UserRepository;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(applicationUser.getUsername(), applicationUser.getPasswordHash(), emptyList());
    }
}
