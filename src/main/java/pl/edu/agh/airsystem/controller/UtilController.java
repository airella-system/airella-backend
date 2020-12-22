package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.airsystem.model.api.response.DataResponse;
import pl.edu.agh.airsystem.model.api.response.Response;
import pl.edu.agh.airsystem.model.api.utils.DateTimeResponse;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/api/utils/")
public class UtilController {

    @GetMapping("datetime")
    public ResponseEntity<? extends Response> getDateTime() {
        Instant instant = Instant.now();
        return ResponseEntity.ok().body(DataResponse.of(new DateTimeResponse(
                instant.toString(),
                instant.getEpochSecond()
        )));
    }

}