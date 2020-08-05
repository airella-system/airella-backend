package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MultipleValueStatistic extends Statistic {

    @OneToMany(mappedBy = "statistic", cascade = CascadeType.REMOVE)
    private List<StatisticValue> values = new ArrayList<>();

    @OneToOne
    private StatisticValue latestStatisticValue;

    public MultipleValueStatistic(String id, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode) {
        super(id, statisticType, statisticPrivacyMode);
    }

    public void addValue(StatisticValue statisticValue) {
        values.add(statisticValue);
        latestStatisticValue = statisticValue;
    }

}
