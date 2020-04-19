package pl.edu.agh.airsystem.model.api.stations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressChangeRequest {
    private String country;
    private String city;
    private String street;
    private String number;
}