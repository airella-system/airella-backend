package pl.edu.agh.airsystem.model.database;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dbId;

    private String id;

    @ManyToOne
    private Station station;

    private SensorType type;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private Set<Measurement> measurements;

    public Sensor(Station station, String id, SensorType sensorType) {
        this.station = station;
        this.id = id;
        this.type = sensorType;
    }

}
