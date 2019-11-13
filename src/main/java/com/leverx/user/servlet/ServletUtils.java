package com.leverx.user.servlet;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.util.stream.Collectors.joining;

public class ServletUtils {
    public static String readJsonBody(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(joining());
    }
}
