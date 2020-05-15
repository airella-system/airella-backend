package pl.edu.agh.airsystem.model.database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserClient extends Client {
    @Column(unique = true)
    private String email;
    private String passwordHash;

    private String stationRegistrationToken = UUID.randomUUID().toString();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private Set<Station> stations = new HashSet<>();

    public UserClient(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;

        addRole(Role.ROLE_USER);
    }
}
