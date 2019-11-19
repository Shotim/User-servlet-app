package com.leverx.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.util.stream.Collectors.joining;

public class ServletUtils {

    private static final String SEPARATOR = "/";
    private static final int ONE = 1;

    public static String readBody(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(joining());
    }

    public static String getPathVariableFromRequest(HttpServletRequest request) {
        var url = request.getRequestURL();
        return getPathVariableFromUrl(url);
    }

    public static String getPathVariableFromUrl(StringBuffer url) {
        var splittedBySlashURL = url.toString().split(SEPARATOR);
        var lastElementIndex = splittedBySlashURL.length - ONE;
        var pathVariable = splittedBySlashURL[lastElementIndex];
        return pathVariable;
    }
}