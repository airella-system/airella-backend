package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.Station;

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
public class MultipleValueFloatStatistic extends Statistic {

    @OneToMany(mappedBy = "statistic", cascade = CascadeType.REMOVE)
    private List<StatisticValue> values = new ArrayList<>();

    @OneToOne
    private StatisticValue latestStatisticValue;

    private String metric;

    public MultipleValueFloatStatistic(String id, String name, String metric, Station station, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode) {
        super(id, name, station, statisticType, statisticPrivacyMode);
        this.metric = metric;
    }

}
