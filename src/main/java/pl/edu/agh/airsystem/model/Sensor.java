package pl.edu.agh.airsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sensor {
    @Id
    private long id;

    @ManyToOne
    private Station station;

    private SensorType type;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private Set<SensorValue> values;
}
