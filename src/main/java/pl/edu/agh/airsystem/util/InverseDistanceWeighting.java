package pl.edu.agh.airsystem.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class InverseDistanceWeighting {

    public static <E> Double apply(List<E> objects,
                                   Function<E, Double> getDistanceFunction,
                                   Function<E, Double> getValueFunction) {
        Optional<Double> upperSum = objects.stream()
                .map(getUpperSumComponent(getDistanceFunction, getValueFunction))
                .reduce(Double::sum);

        Optional<Double> lowerSum = objects.stream()
                .map(getLowerSumComponent(getDistanceFunction))
                .reduce(Double::sum);

        if (upperSum.isPresent() && lowerSum.isPresent()) {
            return upperSum.get() / lowerSum.get();
        }

        return null;
    }

    private static <E> Function<E, Double> getUpperSumComponent(Function<E, Double> getDistanceFunction,
                                                                Function<E, Double> getValueFunction) {
        return object -> Math.pow(1 / getDistanceFunction.apply(object), 2) * getValueFunction.apply(object);
    }

    private static <E> Function<E, Double> getLowerSumComponent(Function<E, Double> getDistanceFunction) {
        return object -> Math.pow(1 / getDistanceFunction.apply(object), 2);
    }

}
