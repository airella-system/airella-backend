package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dbId;

    @Column(unique = true)
    private String id;

    private String name;

    private StationVisibility stationVisibility = StationVisibility.PUBLIC;

    @Embedded
    private Location location = new Location();

    @ManyToOne
    private UserClient owner;

    @OneToOne(cascade = CascadeType.REMOVE)
    private StationClient stationClient;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Address address;

    @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
    private List<Sensor> sensors = new ArrayList<>();

    @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
    private List<Statistic> statistics = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        if (owner != null) { //owner can be null for example for generated stations
            owner.getStations().remove(this);
        }
    }
}
