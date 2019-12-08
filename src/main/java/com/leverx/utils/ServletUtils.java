package com.leverx.utils;

import com.leverx.user.servlet.GetMethodTypes;
import com.leverx.user.servlet.MethodTypePlusRequiredVar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.leverx.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.converter.EntityJsonConverter.fromJsonToEntity;
import static com.leverx.user.servlet.GetMethodTypes.GET_ALL_USERS;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static com.leverx.utils.RequestURLUtils.getEntityReceivedClass;
import static com.leverx.utils.RequestURLUtils.getLastStringElement;
import static com.leverx.utils.RequestURLUtils.getPreLastStringElement;
import static com.leverx.utils.RequestURLUtils.getSplittedUrl;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class ServletUtils {

    public static final int SC_UNPROCESSABLE_ENTITY = 422;
    private static final String USERS = "users";
    private static final String CATS = "cats";

    public static void printValidationErrorMessages(HttpServletResponse response, String message) throws IOException {
        response.setStatus(SC_UNPROCESSABLE_ENTITY);
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
}