package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
public class LinearMultipleStringValueStatisticGeneratorDefinition
        implements GeneratorStatisticValueDefinition {
    private List<String> choices;
    private final Duration stepAfterMin;
    private final Duration stepAfterMax;

    @Override
    public GeneratorStatisticValueInstance createInstance() {
        return new LinearMultipleStringValueStatisticGenerator(choices, stepAfterMin, stepAfterMax);
    }

}
