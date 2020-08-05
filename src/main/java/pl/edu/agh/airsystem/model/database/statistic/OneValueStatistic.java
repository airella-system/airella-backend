package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OneValueStatistic extends Statistic {
    @OneToOne(mappedBy = "statistic", cascade = CascadeType.REMOVE)
    private StatisticValue value;

    public OneValueStatistic(String id, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode) {
        super(id, statisticType, statisticPrivacyMode);
    }

}
