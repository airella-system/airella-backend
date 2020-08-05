package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StatisticValueDouble extends StatisticValue {
    private double value;

    public StatisticValueDouble(Statistic statistic, Instant timestamp, double value) {
        super(statistic, timestamp);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
