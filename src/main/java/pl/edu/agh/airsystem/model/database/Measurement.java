package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "measurement", indexes = {
        @Index(name = "INDX_0", columnList = "timestamp")})
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Sensor sensor;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant timestamp;
    private double value;

    public Measurement(Sensor sensor, Instant timestamp, double value) {
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.value = value;
    }

}
