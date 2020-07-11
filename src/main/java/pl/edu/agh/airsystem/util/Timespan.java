package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Timespan {
    private Instant start;
    private Instant end;
}
