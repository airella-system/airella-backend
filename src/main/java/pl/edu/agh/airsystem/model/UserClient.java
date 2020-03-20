package pl.edu.agh.airsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserClient extends Client {
    @Column(unique = true)
    private String username;
    private String passwordHash;

    public UserClient(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;

        addRole(Role.ROLE_USER);
    }
}
