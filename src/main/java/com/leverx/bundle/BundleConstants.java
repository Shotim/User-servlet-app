package com.leverx.bundle;

import java.util.Locale;
import java.util.ResourceBundle;

public class BundleConstants {

    public static final String FILE_NAME = "messages";
    public static final String CAT_DOES_NOT_EXIST = "CAT_DOES_NOT_EXIST";
    public static final String DOG_DOES_NOT_EXIST = "DOG_DOES_NOT_EXIST";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String EQUAL_SENDER_AND_RECIPIENT = "EQUAL_SENDER_AND_RECIPIENT";
    public static final String NOT_ENOUGH_MONEY = "NOT_ENOUGH_MONEY";

    public static String getLocalizedMessage(String constantName) {
        return ResourceBundle.getBundle(FILE_NAME, Locale.getDefault()).getString(constantName);
    }
}
