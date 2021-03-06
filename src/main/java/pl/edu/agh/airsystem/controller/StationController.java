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
import pl.edu.agh.airsystem.model.api.stations.complexapi.ComplexQueryRequest;
import pl.edu.agh.airsystem.service.StationService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;
    private final MeasurementQueryConverter measurementQueryConverter;

    @GetMapping("")
    public ResponseEntity<? extends Response> getStations() {
        return stationService.getStations();
    }

    @GetMapping("{stationId}")
    public ResponseEntity<? extends Response> getStation(
            @PathVariable(value = "stationId") String stationId,
            MeasurementQueryRequest measurementQueryRequest) {
        return stationService.getStation(stationId,
                measurementQueryConverter.convert(measurementQueryRequest));
    }

    @PostMapping("{stationId}/query")
    public ResponseEntity<? extends Response> makeComplexQuery(
            @PathVariable(value = "stationId") String stationId,
            @RequestBody ComplexQueryRequest complexQueryRequest) {
        return stationService.makeComplexQuery(stationId, complexQueryRequest);
    }

    @DeleteMapping("{stationId}")
    public ResponseEntity<? extends Response> deleteStation(
            @PathVariable(value = "stationId") String stationId) {
        return stationService.deleteStation(stationId);
    }

    @PutMapping("{stationId}/name")
    public ResponseEntity<? extends Response> setName(
            @PathVariable(value = "stationId") String stationId,
            @RequestBody NameChangeRequest nameChangeRequest) {
        return stationService.setStationName(stationId, nameChangeRequest);
    }

    @PutMapping("{stationId}/address")
    public ResponseEntity<? extends Response> setAddress(
            @PathVariable(value = "stationId") String stationId,
            @RequestBody AddressChangeRequest addressChangeRequest) {
        return stationService.setStationAddress(stationId, addressChangeRequest);
    }

    @PutMapping("{stationId}/location")
    public ResponseEntity<? extends Response> setLocation(
            @PathVariable(value = "stationId") String stationId,
            @RequestBody LocationChangeRequest locationChangeRequest) {
        return stationService.setStationLocation(stationId, locationChangeRequest);
    }

}