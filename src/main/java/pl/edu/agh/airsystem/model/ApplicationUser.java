package pl.edu.agh.airsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "\"user\"") //user is reserved name in PostgreSQL, "user" not
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;
    private String passwordHash;
    private String refreshToken;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public ApplicationUser(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}
