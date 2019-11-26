package com.leverx.utils;

import com.leverx.user.servlet.GetMethodTypes;
import com.leverx.user.servlet.Pair;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.leverx.user.servlet.GetMethodTypes.GET_ALL_USERS;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class ServletUtils {

    private static final String SEPARATOR = "/";
    private static final int ONE = 1;
    private static final String USERS = "users";
    private static final String CATS = "cats";

    public static String readBody(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(joining());
    }

    public static String getPathVariableFromRequest(HttpServletRequest request) {
        var url = request.getRequestURL();
        var splittedBySlashURL = url.toString().split(SEPARATOR);
        var lastElementIndex = splittedBySlashURL.length - ONE;
        return splittedBySlashURL[lastElementIndex];
    }

    public static Pair<GetMethodTypes, String> initUserServletGetMethodType(HttpServletRequest request) {
        Pair<GetMethodTypes, String> pair = new Pair<>();
        var requestUrl = request.getRequestURL();
        var splittedUrl = requestUrl.toString().split(SEPARATOR);
        var lastElementIndex = splittedUrl.length - 1;
        var lastElement = splittedUrl[lastElementIndex];
        if (isParsable(lastElement)) {
            pair.setValues(GET_USER_BY_ID, lastElement);
            return pair;
        } else if (USERS.equals(lastElement)) {
            pair.setValues(GET_ALL_USERS, lastElement);
            return pair;
        } else if (CATS.equals(lastElement)) {
            var userId = splittedUrl[lastElementIndex - 1];
            pair.setValues(GET_CATS_OF_USER, userId);
            return pair;
        } else {
            pair.setValues(GET_USER_BY_NAME, lastElement);
            return pair;
        }
    }
}