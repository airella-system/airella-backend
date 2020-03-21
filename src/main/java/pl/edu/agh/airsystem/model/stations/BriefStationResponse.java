package pl.edu.agh.airsystem.model.stations;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.airsystem.model.database.Address;
import pl.edu.agh.airsystem.model.database.Location;
import pl.edu.agh.airsystem.model.database.Station;

@Getter
@Setter
public class BriefStationResponse {
    private final long id;
    private final String name;
    private final Address address;
    private final Location location;

    public BriefStationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
        this.address = station.getAddress();
        this.location = station.getLocation();
    }

}
