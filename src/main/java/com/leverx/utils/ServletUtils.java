package com.leverx.utils;

import com.leverx.user.servlet.GetMethodTypes;
import com.leverx.user.servlet.MethodTypePlusRequiredVar;
import com.leverx.user.servlet.PutMethodTypes;
import com.leverx.validator.message.ValidationErrorsMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.leverx.mapper.EntityJsonMapper.convertFromEntityToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromJsonToEntity;
import static com.leverx.user.servlet.GetMethodTypes.GET_ALL_USERS;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_CAT_BY_ID_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static com.leverx.user.servlet.PutMethodTypes.ASSIGN_CATS_TO_USER;
import static com.leverx.user.servlet.PutMethodTypes.EDIT_USER;
import static com.leverx.user.servlet.PutMethodTypes.MOVE_CAT_TO_ANOTHER_USER;
import static java.util.stream.Collectors.joining;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class ServletUtils {

    private static final String SEPARATOR = "/";
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final String USERS = "users";
    private static final String CATS = "cats";

    public static void printErrorMessages(HttpServletResponse response, ValidationErrorsMessage errors) throws IOException {
        response.setStatus(SC_BAD_REQUEST);
        var messagesJson = convertFromEntityToJson(errors);
        var responseWriter = response.getWriter();
        responseWriter.print(messagesJson);
        responseWriter.flush();
    }

    public static <T> T readJsonBody(HttpServletRequest request, Class<T> tclass) throws IOException {
        var jsonString = readBody(request);
        return convertFromJsonToEntity(jsonString, tclass);
    }

    public static String readBody(HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(joining());
    }

    public static MethodTypePlusRequiredVar<GetMethodTypes, String> initUserServletGetMethodType(HttpServletRequest request) {
        MethodTypePlusRequiredVar<GetMethodTypes, String> methodTypePlusRequiredVar = new MethodTypePlusRequiredVar<>();
        var splittedUrl = getSplittedUrl(request);
        var lastElement = getLastStringElement(splittedUrl);

        if (isParsable(lastElement)) {
            String entityClass = getEntityReceivedClass(splittedUrl).orElseThrow();
            switch (entityClass) {
                case USERS:
                    methodTypePlusRequiredVar.setValues(GET_USER_BY_ID, lastElement);
                    break;
                case CATS:
                    methodTypePlusRequiredVar.setValues(GET_CAT_BY_ID_OF_USER, lastElement);
                    break;
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
        MethodTypePlusRequiredVar<PutMethodTypes, String> methodTypePlusRequiredVar = new MethodTypePlusRequiredVar<>();
        var splittedUrl = getSplittedUrl(request);
        var lastElement = getLastStringElement(splittedUrl);

        if (isParsable(lastElement)) {
            methodTypePlusRequiredVar.setValues(MOVE_CAT_TO_ANOTHER_USER, lastElement);
        } else {

            if (CATS.equals(lastElement)) {
                var ownerId = getPreLastStringElement(splittedUrl);
                methodTypePlusRequiredVar.setValues(ASSIGN_CATS_TO_USER, ownerId);
            } else {
                methodTypePlusRequiredVar.setValues(EDIT_USER, lastElement);
            }
        }

        return methodTypePlusRequiredVar;
    }

    public static String getPathVariableFromRequest(HttpServletRequest request) {
        var splittedBySlashURL = getSplittedUrl(request);
        return getLastStringElement(splittedBySlashURL);
    }

    private static List<String> getSplittedUrl(HttpServletRequest request) {
        var requestUrl = request.getRequestURL();
        String stringUrl = requestUrl.toString();
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