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
public class StatisticValueString extends StatisticValue {
    private String value;

    public StatisticValueString(Statistic statistic, Instant timestamp, String value) {
        super(statistic, timestamp);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
