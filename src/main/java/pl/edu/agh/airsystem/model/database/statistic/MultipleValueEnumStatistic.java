package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.Station;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MultipleValueEnumStatistic extends Statistic {

    @OneToMany(mappedBy = "statistic", cascade = CascadeType.REMOVE)
    private List<StatisticValue> values = new ArrayList<>();

    @OneToOne
    private StatisticValue latestStatisticValue;

    private StatisticChartType chartType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistic_db_id")
    private List<StatisticEnumDefinition> statisticEnumDefinitions;

    public MultipleValueEnumStatistic(String id, String name, List<StatisticEnumDefinition> statisticEnumDefinitions, StatisticChartType chartType, Station station, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode) {
        super(id, name, station, statisticType, statisticPrivacyMode);
        this.statisticEnumDefinitions = statisticEnumDefinitions;
        this.chartType = chartType;
    }

}
