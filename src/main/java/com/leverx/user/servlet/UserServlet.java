package com.leverx.user.servlet;

import com.leverx.cat.mapper.CatJsonMapper;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;
import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static com.leverx.user.mapper.UserJsonMapper.convertFromJsonToUserDto;
import static com.leverx.user.mapper.UserJsonMapper.convertFromUserCollectionToJson;
import static com.leverx.user.mapper.UserJsonMapper.convertFromUserToJson;
import static com.leverx.user.servlet.GetMethodTypes.GET_CATS_OF_USER;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_ID;
import static com.leverx.user.servlet.GetMethodTypes.GET_USER_BY_NAME;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.initUserServletGetMethodType;
import static com.leverx.utils.ServletUtils.readBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;


public class UserServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();
    private CatService catService = new CatServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var responseWriter = response.getWriter();
        var methodTypeWithPathVariable = initUserServletGetMethodType(request);
        var methodType = methodTypeWithPathVariable.entrySet().stream().findFirst().map(Map.Entry::getKey).get();
        switch (methodType) {
            case GET_ALL_USERS:
                printAllUsersToResponseBody(responseWriter);
                break;
            case GET_USER_BY_ID:
                var userId = methodTypeWithPathVariable.get(GET_USER_BY_ID);
                printUserByIdToResponseBody(responseWriter, userId);
                break;
            case GET_USER_BY_NAME:
                var userName = methodTypeWithPathVariable.get(GET_USER_BY_NAME);
                printUsersByNameToResponseBody(responseWriter, userName);
                break;
            case GET_CATS_OF_USER:
                var ownerId = methodTypeWithPathVariable.get(GET_CATS_OF_USER);
                printCatsOfUser(responseWriter, ownerId);
                break;
        }
        responseWriter.flush();
        response.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var jsonUserDto = readBody(request);
        var userDto = convertFromJsonToUserDto(jsonUserDto);

        try {
            userService.save(userDto);
            response.setStatus(SC_CREATED);
        } catch (InternalServerErrorException ex) {
            response.setStatus(SC_BAD_REQUEST);
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
        var id = getPathVariableFromRequest(request);
        var jsonUser = readBody(request);
        var userDto = convertFromJsonToUserDto(jsonUser);

        try {
            userService.updateById(id, userDto);
            response.setStatus(SC_OK);
        } catch (InternalServerErrorException ex) {
            response.setStatus(SC_BAD_REQUEST);
        }

    }

    private void printAllUsersToResponseBody(PrintWriter writer) {
        var users = userService.findAll();
        var jsonUsers = convertFromUserCollectionToJson(users);
        jsonUsers.forEach(writer::println);
    }

    private void printUserByIdToResponseBody(PrintWriter writer, String pathVariable) {
        var id = parseInt(pathVariable);
        var user = userService.findById(id);
        var jsonUser = convertFromUserToJson(user);
        writer.print(jsonUser);
    }

    private void printUsersByNameToResponseBody(PrintWriter writer, String pathVariable) {
        var users = userService.findByName(pathVariable);
        var jsonUsers = convertFromUserCollectionToJson(users);
        jsonUsers.forEach(writer::println);
    }

    private void printCatsOfUser(PrintWriter writer, String ownerId) {
        var id = Integer.parseInt(ownerId);
        var cats = catService.findByOwner(id);
        var jsonCats = CatJsonMapper.convertFromCatCollectionToJson(cats);
        jsonCats.forEach(writer::println);
    }
}