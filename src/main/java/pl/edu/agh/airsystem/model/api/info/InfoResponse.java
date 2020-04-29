package pl.edu.agh.airsystem.model.api.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoResponse {
    private Instant buildDate;
    private String version;
}
