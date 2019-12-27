package com.leverx.user.servlet;

import com.leverx.cat.service.CatService;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.dog.service.DogService;
import com.leverx.pet.service.PetService;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.core.config.BeanFactory.getCatService;
import static com.leverx.core.config.BeanFactory.getDogService;
import static com.leverx.core.config.BeanFactory.getPetService;
import static com.leverx.core.config.BeanFactory.getUserService;
import static com.leverx.core.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.core.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.core.utils.RequestURLUtils.getPathVariableFromRequest;
import static com.leverx.core.utils.ServletUtils.initUserServletGetMethodType;
import static com.leverx.core.utils.ServletUtils.initUserServletPostMethodType;
import static com.leverx.core.utils.ServletUtils.printEntityCollectionToResponseBody;
import static com.leverx.core.utils.ServletUtils.printValidationErrorMessages;
import static com.leverx.core.utils.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;


public class UserServlet extends HttpServlet {

    private static final String RECIPIENT_ID_PARAMETER = "recipientId";
    private static final String SENT_POINTS_PARAMETER = "points";

    private UserService userService;
    private CatService catService;
    private DogService dogService;
    private PetService petService;

    public UserServlet() {
        this.userService = getUserService();
        this.catService = getCatService();
        this.dogService = getDogService();
        this.petService = getPetService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var responseWriter = response.getWriter();
        var methodTypeWithPathVariable = initUserServletGetMethodType(request);
        var methodType = methodTypeWithPathVariable.getMethodType();
        var requiredVariable = methodTypeWithPathVariable.getPathVar();
        var responseStatus = switch (methodType) {
            case GET_ALL_USERS -> printAllUsersToResponseBody(responseWriter);
            case GET_USER_BY_ID -> printUserByIdToResponseBody(responseWriter, requiredVariable);
            case GET_USER_BY_NAME -> printUsersByNameToResponseBody(responseWriter, requiredVariable);
            case GET_CATS_OF_USER -> printCatsOfUser(responseWriter, requiredVariable);
            case GET_DOGS_OF_USER -> printDogsOfUser(responseWriter, requiredVariable);
            case GET_PETS_OF_USER -> printPetsOfUser(responseWriter, requiredVariable);
        };
        response.setStatus(responseStatus);
        responseWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var methodTypePlusRequiredVar = initUserServletPostMethodType(request);
        var methodType = methodTypePlusRequiredVar.getMethodType();
        var variable = methodTypePlusRequiredVar.getPathVar();
        switch (methodType) {
            case CREATE_USER -> createUser(request, response);
            case TRANSFER_POINTS -> transferPoints(request, response, variable);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        var id = getPathVariableFromRequest(request);
        userService.deleteById(id);
        response.setStatus(SC_NO_CONTENT);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var userDto = readJsonBody(request, UserInputDto.class);
        var variable = getPathVariableFromRequest(request);
        try {
            userService.updateById(variable, userDto);
            response.setStatus(SC_OK);
        } catch (ValidationFailedException e) {
            printValidationErrorMessages(response, e);
        }
    }

    private int printAllUsersToResponseBody(PrintWriter writer) {
        var users = userService.findAll();
        printEntityCollectionToResponseBody(writer, users);
        return users.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }

    private int printUserByIdToResponseBody(PrintWriter writer, String pathVariable) {
        var id = parseInt(pathVariable);
        try {
            var user = userService.findById(id);
            var jsonUser = fromEntityToJson(user);
            writer.print(jsonUser);
            return SC_OK;
        } catch (ElementNotFoundException e) {
            return e.getStatusCode();
        }
    }

    private int printUsersByNameToResponseBody(PrintWriter writer, String pathVariable) {
        var users = userService.findByName(pathVariable);
        var jsonUsers = fromEntityCollectionToJson(users);
        jsonUsers.forEach(writer::println);
        return users.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }

    private int printCatsOfUser(PrintWriter writer, String ownerId) {
        var id = parseInt(ownerId);
        var cats = catService.findByOwner(id);
        printEntityCollectionToResponseBody(writer, cats);
        return cats.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }

    private int printDogsOfUser(PrintWriter writer, String ownerId) {
        var id = parseInt(ownerId);
        var dogs = dogService.findByOwner(id);
        printEntityCollectionToResponseBody(writer, dogs);
        return dogs.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }

    private int printPetsOfUser(PrintWriter writer, String ownerId) {
        var id = parseInt(ownerId);
        var pets = petService.findByOwner(id);
        printEntityCollectionToResponseBody(writer, pets);
        return pets.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }

    private void transferPoints(HttpServletRequest request, HttpServletResponse response, String variable) throws IOException {
        try {
            var recipientId = request.getParameter(RECIPIENT_ID_PARAMETER);
            var points = request.getParameter(SENT_POINTS_PARAMETER);
            userService.pointsTransfer(variable, recipientId, points);
        } catch (ValidationFailedException e) {
            printValidationErrorMessages(response, e);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var userDto = readJsonBody(request, UserInputDto.class);
        try {
            userService.save(userDto);
            response.setStatus(SC_CREATED);
        } catch (ValidationFailedException e) {
            printValidationErrorMessages(response, e);
        }
    }
}