package com.leverx.user.servlet;

import com.leverx.cat.entity.CatsDtoIdsList;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;
import com.leverx.user.entity.UserDto;
import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.mapper.EntityJsonMapper.convertFromEntityCollectionToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromEntityToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromJsonToEntity;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.initUserServletGetMethodType;
import static com.leverx.utils.ServletUtils.initUserServletPutMethodType;
import static com.leverx.utils.ServletUtils.readBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;


public class UserServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();
    private CatService catService = new CatServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var responseWriter = response.getWriter();
        var methodTypeWithPathVariable = initUserServletGetMethodType(request);
        var methodType = methodTypeWithPathVariable.getLeft();
        var requiredVariable = methodTypeWithPathVariable.getRight();
        var responseStatus = SC_OK;
        switch (methodType) {
            case GET_ALL_USERS:
                responseStatus = printAllUsersToResponseBody(responseWriter);
                break;
            case GET_USER_BY_ID:
                responseStatus = printUserByIdToResponseBody(responseWriter, requiredVariable);
                break;
            case GET_USER_BY_NAME:
                responseStatus = printUsersByNameToResponseBody(responseWriter, requiredVariable);
                break;
            case GET_CATS_OF_USER:
                responseStatus = printCatsOfUser(responseWriter, requiredVariable);
                break;
        }
        responseWriter.flush();
        response.setStatus(responseStatus);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var jsonUserDto = readBody(request);
        var userDto = convertFromJsonToEntity(jsonUserDto, UserDto.class);
        try {
            userService.save(userDto);
            response.setStatus(SC_CREATED);
        } catch (IllegalArgumentException e) {
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

        var methodTypeWithPathVariable = initUserServletPutMethodType(request);
        var methodType = methodTypeWithPathVariable.getLeft();
        var pathVariable = methodTypeWithPathVariable.getRight();
        switch (methodType) {
            case EDIT_USER: {
                var jsonUser = readBody(request);
                var userDto = convertFromJsonToEntity(jsonUser, UserDto.class);
                try {
                    userService.updateById(pathVariable, userDto);
                    response.setStatus(SC_OK);
                } catch (IllegalArgumentException e) {
                    response.setStatus(SC_BAD_REQUEST);
                }
            }
            break;
            case ASSIGN_CATS_TO_USER: {
                var jsonIdList = readBody(request);
                var catsIds = convertFromJsonToEntity(jsonIdList, CatsDtoIdsList.class);
                var catsIdsList = catsIds.getIds();
                var ownerId = parseInt(pathVariable);
                try {
                    userService.assignCatsToUser(ownerId, catsIdsList);
                } catch (NullPointerException e) {
                    response.setStatus(SC_BAD_REQUEST);
                }
            }
        }
    }

    private int printAllUsersToResponseBody(PrintWriter writer) {
        var users = userService.findAll();
        var jsonUsers = convertFromEntityCollectionToJson(users);
        jsonUsers.forEach(writer::println);
        return users.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }

    private int printUserByIdToResponseBody(PrintWriter writer, String pathVariable) {
        var id = parseInt(pathVariable);
        var user = userService.findById(id);
        var jsonUser = convertFromEntityToJson(user);
        writer.print(jsonUser);
        return user != null ? SC_OK : SC_NOT_FOUND;
    }

    private int printUsersByNameToResponseBody(PrintWriter writer, String pathVariable) {
        var users = userService.findByName(pathVariable);
        var jsonUsers = convertFromEntityCollectionToJson(users);
        jsonUsers.forEach(writer::println);
        return users.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }

    private int printCatsOfUser(PrintWriter writer, String ownerId) {
        var id = parseInt(ownerId);
        var cats = catService.findByOwner(id);
        var jsonCats = convertFromEntityCollectionToJson(cats);
        jsonCats.forEach(writer::println);
        return cats.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }
}