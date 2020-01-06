package com.leverx.core.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RequestURLUtils {

    private static final String SEPARATOR = "/";
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final String USERS = "users";
    private static final String CATS = "cats";

    public static String getPathVariableFromRequest(HttpServletRequest request) {
        var splittedBySlashURL = getSplittedUrl(request);
        return getLastStringElement(splittedBySlashURL);
    }

    public static List<String> getSplittedUrl(HttpServletRequest request) {
        var requestUrl = request.getRequestURL();
        var stringUrl = requestUrl.toString();
        return Arrays.asList(stringUrl.split(SEPARATOR));
    }

    public static String getLastStringElement(List<String> splittedUrl) {
        var lastElementIndex = splittedUrl.size() - ONE;
        return splittedUrl.get(lastElementIndex);
    }

    public static String getPreLastStringElement(List<String> splittedUrl) {
        var requiredElementIndex = splittedUrl.size() - TWO;
        return splittedUrl.get(requiredElementIndex);
    }

    public static Optional<String> getEntityReceivedClass(List<String> splittedUrl) {
        if (splittedUrl.contains(USERS)) {

            if (splittedUrl.contains(CATS)) {
                return Optional.of(CATS);
            }
            return Optional.of(USERS);
        }
        return Optional.empty();
    }
}
