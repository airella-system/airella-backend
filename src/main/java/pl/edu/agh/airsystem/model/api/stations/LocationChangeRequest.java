package pl.edu.agh.airsystem.model.api.stations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationChangeRequest {
    Double latitude;
    Double longitude;
}