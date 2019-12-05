package com.leverx.utils;

import com.leverx.user.servlet.GetMethodTypes;
import com.leverx.user.servlet.MethodTypePlusRequiredVar;
import com.leverx.user.servlet.PutMethodTypes;
import com.leverx.validator.message.ValidationErrorsMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.leverx.mapper.EntityJsonMapper.convertFromEntityToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromJsonToEntity;
import static com.leverx.user.servlet.GetMethodTypes.GET_ALL_USERS;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static com.leverx.user.servlet.PutMethodTypes.ASSIGN_CATS_TO_USER;
import static com.leverx.user.servlet.PutMethodTypes.EDIT_USER;
import static java.util.stream.Collectors.joining;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class ServletUtils {

    private static final String SEPARATOR = "/";
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final String USERS_ORIGIN = "users";
    private static final String CATS_ORIGIN = "cats";

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

    public static String getPathVariableFromRequest(HttpServletRequest request) {
        var splittedBySlashURL = getSplittedUrl(request);
        return getLastStringElement(splittedBySlashURL);
    }

    public static MethodTypePlusRequiredVar<GetMethodTypes, String> initUserServletGetMethodType(HttpServletRequest request) {
        MethodTypePlusRequiredVar<GetMethodTypes, String> methodTypePlusRequiredVar = new MethodTypePlusRequiredVar<>();
        var splittedUrl = getSplittedUrl(request);
        var lastElement = getLastStringElement(splittedUrl);

        if (isParsable(lastElement)) {
            methodTypePlusRequiredVar.setValues(GET_USER_BY_ID, lastElement);

        } else {
            switch (lastElement) {
                case USERS_ORIGIN:
                    methodTypePlusRequiredVar.setValues(GET_ALL_USERS, lastElement);
                    break;
                case CATS_ORIGIN:
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

        if (CATS_ORIGIN.equals(lastElement)) {
            var ownerId = getPreLastStringElement(splittedUrl);
            methodTypePlusRequiredVar.setValues(ASSIGN_CATS_TO_USER, ownerId);
        } else {
            methodTypePlusRequiredVar.setValues(EDIT_USER, lastElement);
        }
        return methodTypePlusRequiredVar;
    }

    private static String[] getSplittedUrl(HttpServletRequest request) {
        var requestUrl = request.getRequestURL();
        String stringUrl = requestUrl.toString();
        return stringUrl.split(SEPARATOR);
    }

    private static String getLastStringElement(String[] splittedUrl) {
        var lastElementIndex = splittedUrl.length - ONE;
        return splittedUrl[lastElementIndex];
    }

    private static String getPreLastStringElement(String[] splittedUrl) {
        var requiredElementIndex = splittedUrl.length - TWO;
        return splittedUrl[requiredElementIndex];
    }
}