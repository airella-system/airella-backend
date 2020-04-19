package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Timespan {
    private LocalDateTime start;
    private LocalDateTime end;
}
