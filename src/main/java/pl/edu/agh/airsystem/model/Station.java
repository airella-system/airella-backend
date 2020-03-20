package pl.edu.agh.airsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Location location;

    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
    private Set<Sensor> sensors;
}
