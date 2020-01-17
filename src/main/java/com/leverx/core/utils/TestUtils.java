package com.leverx.core.utils;

import java.time.LocalDate;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class TestUtils {

    public static int id() {
        return nextInt(0, 1000);
    }

    public static String validName() {
        return random(11);
    }

    public static String invalidName() {
        return random(4);
    }

    public static int validMiceCaughtNumber() {
        return nextInt(0, 1000);
    }

    public static int randomInvalidMiceCaughtNumber() {
        return -nextInt(1, 1000);
    }

    public static LocalDate validDateOfBirth() {
        return LocalDate.of(nextInt(1970, 2019), nextInt(1, 12), nextInt(1, 29));
    }

    public static LocalDate invalidDateOfBirth() {
        return LocalDate.of(nextInt(2021, 2100), nextInt(1, 12), nextInt(1, 29));
    }

    public static String validEmail() {
        return random(7) + "@" + random(4);
    }

    public static String invalidEmail() {
        return random(15);
    }

    public static boolean cutEars() {
        return nextBoolean();
    }

    public static int validAnimalPoints() {
        return nextInt(0, 1000);
    }

    public static int invalidAnimalPoints() {
        return -nextInt(1, 1000);
    }

}