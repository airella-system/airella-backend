package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"station_db_id", "id"})})
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dbId;

    private String id;

    @ManyToOne
    @JoinColumn(name = "station_db_id")
    private Station station;

    private SensorType type;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private Set<Measurement> measurements = new HashSet<>();

    @OneToOne(cascade = CascadeType.REMOVE)
    private Measurement latestMeasurement;

    public Sensor(Station station, String id, SensorType sensorType) {
        this.station = station;
        this.id = id;
        this.type = sensorType;
    }

}
