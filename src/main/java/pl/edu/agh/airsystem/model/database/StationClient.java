package pl.edu.agh.airsystem.model.database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class StationClient extends Client {
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "stationClient")
    private Station station;

    public StationClient(Station station) {
        this.station = station;

        addRole(Role.ROLE_STATION);
    }
}
