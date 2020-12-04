package pl.edu.agh.airsystem.model.database.statistic;

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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "statisticValue", indexes = {
        @Index(name = "INDX_1", columnList = "timestamp")})
public abstract class StatisticValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dbId;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant timestamp;

    @ManyToOne
    private Statistic statistic;

    public StatisticValue(Statistic statistic, Instant timestamp) {
        this.statistic = statistic;
        this.timestamp = timestamp;
    }

    public abstract Object getValue();
}
