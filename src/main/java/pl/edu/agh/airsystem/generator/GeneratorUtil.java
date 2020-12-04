package pl.edu.agh.airsystem.generator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class GeneratorUtil {
    @Value("${airella.station.generator.enabled}")
    private boolean generatorEnabled;
}
