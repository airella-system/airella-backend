package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Sensor sensor;

    private Timestamp timestamp;
    private double value;

    public Measurement(Sensor sensor, Timestamp timestamp, double value) {
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.value = value;
    }

}
