package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.Station;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OneValueStringStatistic extends Statistic {
    @OneToOne(mappedBy = "statistic", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private StatisticValue value;

    public OneValueStringStatistic(String id, String name, Station station, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode) {
        super(id, name, station, statisticType, statisticPrivacyMode);
    }

}
