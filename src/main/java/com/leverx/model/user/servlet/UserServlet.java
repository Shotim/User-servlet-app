package com.leverx.model.user.servlet;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.model.cat.service.CatService;
import com.leverx.model.dog.service.DogService;
import com.leverx.model.pet.service.PetService;
import com.leverx.model.user.dto.UserInputDto;
import com.leverx.model.user.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.difactory.DIFactory.getBean;
import static com.leverx.utils.RequestURLUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.initUserServletGetMethodType;
import static com.leverx.utils.ServletUtils.printEntityCollectionToResponseBody;
import static com.leverx.utils.ServletUtils.printValidationErrorMessages;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;


public class UserServlet extends HttpServlet {

    private final UserService userService = getBean(UserService.class);
    private final CatService catService = getBean(CatService.class);
    private final DogService dogService = getBean(DogService.class);
    private final PetService petService = getBean(PetService.class);

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
        var userDto = readJsonBody(request, UserInputDto.class);
        try {
            userService.save(userDto);
            response.setStatus(SC_CREATED);
        } catch (ValidationFailedException e) {
            printValidationErrorMessages(response, e);
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
        var pathVariable = getPathVariableFromRequest(request);
        try {
            userService.updateById(pathVariable, userDto);
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

}