package pl.edu.agh.airsystem.generator;

import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public class LinearMultipleDoubleValueStatisticGeneratorDefinition
        implements GeneratorStatisticValueDefinition {
    private final double min;
    private final double max;
    private final double minStep;
    private final double maxStep;
    private final Duration stepAfterMin;
    private final Duration stepAfterMax;

    @Override
    public GeneratorStatisticValueInstance createInstance() {
        return new LinearMultipleDoubleValueStatisticGenerator(min, max, minStep, maxStep, stepAfterMin, stepAfterMax);
    }

}
