package pl.edu.agh.airsystem.util;

public class RandomUtils {

    public static double randomBetween(double min, double max) {
        return min + (max - min) * Math.random();
    }

    public static int randomBetween(int min, int max) {
        return (int) (min + (max - min) * Math.random());
    }

}
