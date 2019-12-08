package com.leverx.utils;

import com.leverx.user.servlet.GetMethodTypes;
import com.leverx.user.servlet.MethodTypePlusRequiredVar;
import com.leverx.user.servlet.PutMethodTypes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.leverx.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.converter.EntityJsonConverter.fromJsonToEntity;
import static com.leverx.user.servlet.GetMethodTypes.GET_ALL_USERS;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static com.leverx.user.servlet.PutMethodTypes.EDIT_USER;
import static java.util.stream.Collectors.joining;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class ServletUtils {

    private static final String SEPARATOR = "/";
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final String USERS = "users";
    private static final String CATS = "cats";

    public static void printErrorMessages(HttpServletResponse response, String message) throws IOException {
        response.setStatus(SC_BAD_REQUEST);
        var messagesJson = fromEntityToJson(message);
        var responseWriter = response.getWriter();
        responseWriter.print(messagesJson);
        responseWriter.flush();
    }

    public static <T> T readJsonBody(HttpServletRequest request, Class<T> tclass) throws IOException {
        var jsonString = readBody(request);
        return fromJsonToEntity(jsonString, tclass);
    }

    public static String readBody(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(joining());
    }

    public static MethodTypePlusRequiredVar<GetMethodTypes, String> initUserServletGetMethodType(HttpServletRequest request) {
        var methodTypePlusRequiredVar = new MethodTypePlusRequiredVar<GetMethodTypes, String>();
        var splittedUrl = getSplittedUrl(request);
        var lastElement = getLastStringElement(splittedUrl);

        if (isParsable(lastElement)) {
            String entityClass = getEntityReceivedClass(splittedUrl).orElseThrow();
            if (USERS.equals(entityClass)) {
                methodTypePlusRequiredVar.setValues(GET_USER_BY_ID, lastElement);
            }

        } else {
            switch (lastElement) {
                case USERS:
                    methodTypePlusRequiredVar.setValues(GET_ALL_USERS, lastElement);
                    break;
                case CATS:
                    var userId = getPreLastStringElement(splittedUrl);
                    methodTypePlusRequiredVar.setValues(GET_CATS_OF_USER, userId);
                    break;
                default:
                    methodTypePlusRequiredVar.setValues(GET_USER_BY_NAME, lastElement);
                    break;
            }
        }
        return methodTypePlusRequiredVar;
    }

    public static MethodTypePlusRequiredVar<PutMethodTypes, String> initUserServletPutMethodType(HttpServletRequest request) {
        var methodTypePlusRequiredVar = new MethodTypePlusRequiredVar<PutMethodTypes, String>();
        var splittedUrl = getSplittedUrl(request);
        var lastElement = getLastStringElement(splittedUrl);
        methodTypePlusRequiredVar.setValues(EDIT_USER, lastElement);
        return methodTypePlusRequiredVar;
    }

    public static String getPathVariableFromRequest(HttpServletRequest request) {
        var splittedBySlashURL = getSplittedUrl(request);
        return getLastStringElement(splittedBySlashURL);
    }

    private static List<String> getSplittedUrl(HttpServletRequest request) {
        var requestUrl = request.getRequestURL();
        var stringUrl = requestUrl.toString();
        return Arrays.asList(stringUrl.split(SEPARATOR));
    }

    private static String getLastStringElement(List<String> splittedUrl) {
        var lastElementIndex = splittedUrl.size() - ONE;
        return splittedUrl.get(lastElementIndex);
    }

    private static String getPreLastStringElement(List<String> splittedUrl) {
        var requiredElementIndex = splittedUrl.size() - TWO;
        return splittedUrl.get(requiredElementIndex);
    }

    private static Optional<String> getEntityReceivedClass(List<String> splittedUrl) {
        if (splittedUrl.contains(USERS)) {

            if (splittedUrl.contains(CATS)) {
                return Optional.of(CATS);
            }
            return Optional.of(USERS);
        }
        return Optional.empty();
    }
}