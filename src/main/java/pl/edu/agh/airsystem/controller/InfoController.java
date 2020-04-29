package pl.edu.agh.airsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.agh.airsystem.model.api.DataResponse;
import pl.edu.agh.airsystem.model.api.info.InfoResponse;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class InfoController {
    private final BuildProperties buildProperties;

    @GetMapping("/info")
    public ResponseEntity<DataResponse> getInfo() {
        return ResponseEntity.ok(
                new DataResponse(
                        new InfoResponse(buildProperties.getTime(),
                                buildProperties.getVersion())
                )
        );
    }

}