package com.leverx.user.servlet;

import com.leverx.user.mapper.UserJsonMapper;
import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.user.mapper.UserJsonMapper.convertFromJsonToUserDto;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.readBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;


public class UserServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "users";
    private UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var responseWriter = response.getWriter();
        var pathVariable = getPathVariableFromRequest(request);

        if (ORIGIN_PATH.equals(pathVariable)) {
            printAllUsersToResponseBody(responseWriter);
        } else {
            printUsersBySpecificParameterToResponseBody(responseWriter, pathVariable);
        }
        responseWriter.flush();
        response.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var jsonDTOUser = readBody(request);
        var userDto = convertFromJsonToUserDto(jsonDTOUser);

        try {
            service.save(userDto);
            response.setStatus(SC_CREATED);
        } catch (InternalServerErrorException ex) {
            response.setStatus(SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        var id = getPathVariableFromRequest(request);
        service.deleteById(id);
        response.setStatus(SC_NO_CONTENT);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var id = getPathVariableFromRequest(request);
        var jsonUser = readBody(request);
        var userDto = convertFromJsonToUserDto(jsonUser);

        try {
            service.updateById(id, userDto);
            response.setStatus(SC_OK);
        } catch (InternalServerErrorException ex) {
            response.setStatus(SC_BAD_REQUEST);
        }

    }

    private void printAllUsersToResponseBody(PrintWriter writer) {
        var users = service.findAll();
        var jsonUsers = UserJsonMapper.convertFromUsersToJson(users);
        jsonUsers.forEach(writer::println);
    }

    private void printUsersBySpecificParameterToResponseBody(PrintWriter writer, String pathVariable) {
        if (isParsable(pathVariable)) {
            printUserByIdToResponseBody(writer, pathVariable);
        } else {
            printUsersByNameToResponseBody(writer, pathVariable);
        }
    }

    private void printUserByIdToResponseBody(PrintWriter writer, String pathVariable) {
        var id = parseInt(pathVariable);
        var user = service.findById(id);
        var jsonUser = UserJsonMapper.convertFromUserToJson(user);
        writer.print(jsonUser);
    }

    private void printUsersByNameToResponseBody(PrintWriter writer, String pathVariable) {
        var users = service.findByName(pathVariable);
        var jsonUsers = UserJsonMapper.convertFromUsersToJson(users);
        jsonUsers.forEach(writer::println);
    }
}