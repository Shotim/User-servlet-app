package com.leverx.utils;

import com.leverx.user.servlet.GetMethodTypes;
import com.leverx.user.servlet.Pair;
import com.leverx.user.servlet.PutMethodTypes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.leverx.user.servlet.GetMethodTypes.GET_ALL_USERS;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static com.leverx.user.servlet.PutMethodTypes.ASSIGN_CATS_TO_USER;
import static com.leverx.user.servlet.PutMethodTypes.EDIT_USER;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class ServletUtils {

    private static final String SEPARATOR = "/";
    private static final int ONE = 1;
    private static final String USERS_ORIGIN = "users";
    private static final String CATS_ORIGIN = "cats";

    public static String readBody(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(joining());
    }

    public static String getPathVariableFromRequest(HttpServletRequest request) {
        String[] splittedBySlashURL = getSplittedUrl(request);
        var lastElementIndex = splittedBySlashURL.length - ONE;
        return splittedBySlashURL[lastElementIndex];
    }

    public static Pair<GetMethodTypes, String> initUserServletGetMethodType(HttpServletRequest request) {
        Pair<GetMethodTypes, String> pair = new Pair<>();
        String[] splittedUrl = getSplittedUrl(request);
        var lastElementIndex = splittedUrl.length - ONE;
        var lastElement = splittedUrl[lastElementIndex];
        if (isParsable(lastElement)) {
            pair.setValues(GET_USER_BY_ID, lastElement);

        } else if (USERS_ORIGIN.equals(lastElement)) {
            pair.setValues(GET_ALL_USERS, lastElement);

        } else if (CATS_ORIGIN.equals(lastElement)) {
            var userId = splittedUrl[lastElementIndex - ONE];
            pair.setValues(GET_CATS_OF_USER, userId);

        } else {
            pair.setValues(GET_USER_BY_NAME, lastElement);
        }
        return pair;
    }

    public static Pair<PutMethodTypes, String> initUserServletPutMethodType(HttpServletRequest request) {
        Pair<PutMethodTypes, String> pair = new Pair<>();
        String[] splittedUrl = getSplittedUrl(request);
        var lastElementIndex = splittedUrl.length - ONE;
        var lastElement = splittedUrl[lastElementIndex];

        if (CATS_ORIGIN.equals(lastElement)) {
            var ownerId = splittedUrl[lastElementIndex - ONE];
            pair.setValues(ASSIGN_CATS_TO_USER, ownerId);
        } else {
            pair.setValues(EDIT_USER, lastElement);
        }
        return pair;
    }

    private static String[] getSplittedUrl(HttpServletRequest request) {
        var requestUrl = request.getRequestURL();
        return requestUrl.toString().split(SEPARATOR);
    }
}