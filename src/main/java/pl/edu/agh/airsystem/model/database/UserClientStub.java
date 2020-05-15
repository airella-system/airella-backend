package pl.edu.agh.airsystem.model.database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserClientStub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;
    private String passwordHash;
    private String activateString;

    public UserClientStub(String email, String passwordHash, String activateString) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.activateString = activateString;
    }

}
