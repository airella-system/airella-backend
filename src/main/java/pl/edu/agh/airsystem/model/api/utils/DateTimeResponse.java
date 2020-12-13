package pl.edu.agh.airsystem.model.api.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateTimeResponse {
    String iso8601utc;
    long unixTimestamp;
}
