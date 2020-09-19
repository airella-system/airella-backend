package pl.edu.agh.airsystem.model.database.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"station_db_id", "id"})})
public abstract class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dbId;

    private String id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "station_db_id")
    private Station station;

    private StatisticType statisticType;

    private StatisticPrivacyMode statisticPrivacyMode;

    public Statistic(String id, String name, Station station, StatisticType statisticType, StatisticPrivacyMode statisticPrivacyMode) {
        this.id = id;
        this.name = name;
        this.station = station;
        this.statisticType = statisticType;
        this.statisticPrivacyMode = statisticPrivacyMode;
    }

}
