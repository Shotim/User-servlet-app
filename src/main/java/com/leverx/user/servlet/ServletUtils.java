package com.leverx.user.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.util.stream.Collectors.joining;

public class ServletUtils {

    public static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);

    public static String readJsonBody(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(joining());
    }

    public static String getPathVariable(StringBuffer url) {
        String[] splittedBySlashURL = url.toString().split("/");
        var lastElementIndex = splittedBySlashURL.length - 1;
        var pathVariable = splittedBySlashURL[lastElementIndex];
        return pathVariable;
    }

    public static boolean variableIsANumber(String pathVariable) {
        try {
            Integer.parseInt(pathVariable);
            return true;
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }
}