package pl.edu.agh.airsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Station {
    @Id
    private long id;
    private String name;
    private Location location;
    private Address address;

    @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
    private Set<Sensor> sensors;
}
