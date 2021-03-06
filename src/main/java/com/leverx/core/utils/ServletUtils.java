package com.leverx.core.utils;

import com.leverx.core.exception.ValidationFailedException;
import com.leverx.user.servlet.GetMethodTypes;
import com.leverx.user.servlet.MethodTypePlusRequiredVar;
import com.leverx.user.servlet.PostMethodTypes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import static com.leverx.core.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.core.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.core.converter.EntityJsonConverter.fromJsonToEntity;
import static com.leverx.core.utils.RequestURLUtils.getEntityReceivedClass;
import static com.leverx.core.utils.RequestURLUtils.getLastStringElement;
import static com.leverx.core.utils.RequestURLUtils.getPreLastStringElement;
import static com.leverx.core.utils.RequestURLUtils.getSplittedUrl;
import static com.leverx.user.servlet.GetMethodTypes.GET_ALL_USERS;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_DOGS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_PETS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static com.leverx.user.servlet.PostMethodTypes.CREATE_USER;
import static com.leverx.user.servlet.PostMethodTypes.TRANSFER_POINTS;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class ServletUtils {

    private static final String USERS = "users";
    private static final String ACTION_TRANSFER = "transfer";
    private static final String CATS = "cats";
    private static final String DOGS = "dogs";
    private static final String PETS = "pets";
    private static final String USER_NAME = "name";

    public static void printValidationErrorMessages(HttpServletResponse response, ValidationFailedException e) throws IOException {
        var messagesJson = fromEntityToJson(e.getMessage());
        var responseWriter = response.getWriter();
        responseWriter.print(messagesJson);
        response.setStatus(e.getStatusCode());
        responseWriter.flush();
    }

    public static <T> void printEntityCollectionToResponseBody(PrintWriter writer, Collection<T> entities) {
        var jsonEntities = fromEntityCollectionToJson(entities);
        jsonEntities.forEach(writer::println);
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

        var parameter = request.getParameter(USER_NAME);
        if (nonNull(parameter)) {
            methodTypePlusRequiredVar.setValues(GET_USER_BY_NAME, parameter);
        } else if (isParsable(lastElement)) {
            String entityClass = getEntityReceivedClass(splittedUrl).orElseThrow();
            if (USERS.equals(entityClass)) {
                methodTypePlusRequiredVar.setValues(GET_USER_BY_ID, lastElement);
            }

        } else {
            String userId;
            switch (lastElement) {
                case USERS -> methodTypePlusRequiredVar.setValues(GET_ALL_USERS, lastElement);
                case CATS -> {
                    userId = getPreLastStringElement(splittedUrl);
                    methodTypePlusRequiredVar.setValues(GET_CATS_OF_USER, userId);
                }
                case DOGS -> {
                    userId = getPreLastStringElement(splittedUrl);
                    methodTypePlusRequiredVar.setValues(GET_DOGS_OF_USER, userId);
                }
                case PETS -> {
                    userId = getPreLastStringElement(splittedUrl);
                    methodTypePlusRequiredVar.setValues(GET_PETS_OF_USER, userId);
                }
            }
        }
        return methodTypePlusRequiredVar;
    }

    public static MethodTypePlusRequiredVar<PostMethodTypes, String> initUserServletPostMethodType(HttpServletRequest request) {
        var methodTypePlusRequiredVar = new MethodTypePlusRequiredVar<PostMethodTypes, String>();
        var splittedUrl = getSplittedUrl(request);
        String lastElement = getLastStringElement(splittedUrl);
        switch (lastElement) {
            case USERS -> methodTypePlusRequiredVar.setValues(CREATE_USER, lastElement);
            case ACTION_TRANSFER -> {
                var senderId = getPreLastStringElement(splittedUrl);
                methodTypePlusRequiredVar.setValues(TRANSFER_POINTS, senderId);
            }
        }
        return methodTypePlusRequiredVar;
    }
}