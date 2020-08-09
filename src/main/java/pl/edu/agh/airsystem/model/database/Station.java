package pl.edu.agh.airsystem.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;

import javax.persistence.*;
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

    @OneToOne
    private StationClient stationClient;

    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
    private List<Sensor> sensors = new ArrayList<>();

    @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
    private List<Statistic> statistics = new ArrayList<>();
}
