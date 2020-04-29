package pl.edu.agh.airsystem.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pair<T, T1> {
    private final T key;
    private final T1 value;
}
