package pl.edu.agh.airsystem.util;

public class RandomUtils {

    public static double randomBetween(double min, double max) {
        return min + (max - min) * Math.random();
    }

}
