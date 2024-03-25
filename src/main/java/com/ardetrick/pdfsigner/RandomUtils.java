package com.ardetrick.pdfsigner;

import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static int nextIntExclusive(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static float getRandomFloatBetween(float min, float max) {
        return min + RANDOM.nextFloat() * (max - min);
    }

    public static double getRandomDouble(double min, double max) {
        return min + RANDOM.nextDouble() * (max - min);
    }

}
