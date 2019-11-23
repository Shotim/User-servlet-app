package com.leverx.utils;

import com.leverx.user.servlet.GetMethodTypes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<GetMethodTypes, String> initUserServletGetMethodType(HttpServletRequest request) {
        Map<GetMethodTypes, String> map = new HashMap<>();
        var requestUrl = request.getRequestURL();
        var splittedUrl = requestUrl.toString().split(SEPARATOR);
        int lastElementIndex = splittedUrl.length - 1;
        var lastElement = splittedUrl[lastElementIndex];
        if (isParsable(lastElement)) {
            map.put(GET_USER_BY_ID, lastElement);
            return map;
        } else if (USERS.equals(lastElement)) {
            map.put(GET_ALL_USERS, lastElement);
            return map;
        } else if (CATS.equals(lastElement)) {
            var userId = splittedUrl[lastElementIndex - 1];
            map.put(GET_CATS_OF_USER, userId);
            return map;
        } else {
            map.put(GET_USER_BY_NAME, lastElement);
            return map;
        }
    }
}