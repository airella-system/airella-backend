package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.converter.MeasurementQueryConverter;
import pl.edu.agh.airsystem.model.api.query.MeasurementQueryRequest;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.stations.AddressChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.LocationChangeRequest;
import pl.edu.agh.airsystem.model.api.stations.NameChangeRequest;
import pl.edu.agh.airsystem.service.StationService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/stations")
public class UserStationController {
    private final StationService stationService;

    @GetMapping("")
    public ResponseEntity<? extends Response> getStations() {
        return stationService.getCurrentUserStations();
    }

}