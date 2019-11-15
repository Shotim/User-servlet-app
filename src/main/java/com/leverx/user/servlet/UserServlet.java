package com.leverx.user.servlet;

import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.user.mapper.UserJsonMapper.convertFromJsonToDTOUser;
import static com.leverx.user.mapper.UserJsonMapper.convertToJson;
import static com.leverx.user.validation.UserValidation.isValidName;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@WebServlet(name = "userServlet", urlPatterns = {"/users", "/users/*"})
public class UserServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "users";
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
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
        var jsonDTOUser = readJsonBody(request);
        var userDTO = convertFromJsonToDTOUser(jsonDTOUser);
        if (isValidName(userDTO)) {
            service.save(userDTO);
            response.setStatus(SC_CREATED);
        } else {
            delegateErrorMessage(response);
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
        var jsonUser = readJsonBody(request);
        var userDTO = convertFromJsonToDTOUser(jsonUser);
        if (isValidName(userDTO)) {
            service.updateById(id, userDTO);
            response.setStatus(SC_OK);
        } else {
            delegateErrorMessage(response);
        }
    }

    private void printAllUsersToResponseBody(PrintWriter writer) {
        var users = service.findAll();
        var jsonUsers = convertToJson(users);
        jsonUsers.forEach(writer::println);
        logger.info("Was received {} users", users.size());
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
        var jsonUser = convertToJson(user);
        writer.print(jsonUser);
        logger.info("");
    }

    private void printUsersByNameToResponseBody(PrintWriter writer, String pathVariable) {
        var users = service.findByName(pathVariable);
        var jsonUsers = convertToJson(users);
        jsonUsers.forEach(writer::println);
    }

    private void delegateErrorMessage(HttpServletResponse response) {
        response.setStatus(SC_BAD_REQUEST);
        logger.error("The name has more than 60 symbols");
        throw new IllegalArgumentException();
    }
}