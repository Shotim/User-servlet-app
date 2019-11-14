package com.leverx.user.servlet;

import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    private static final String PATH = "users";
    private UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var out = response.getWriter();

        var pathVariable = getPathVariableFromRequest(request);
        //TODO Find solution to replace if,else,else statement
        if (PATH.equals(pathVariable)) {
            var users = service.findAll();
            var jsonUsers = convertToJson(users);
            jsonUsers.forEach(out::print);
        } else if (isParsable(pathVariable)) {
            var id = parseInt(pathVariable);
            var user = service.findById(id);
            var jsonUser = convertToJson(user);
            out.print(jsonUser);
        } else {
            service.findByName(pathVariable)
                    .forEach(out::print);
            var users = service.findByName(pathVariable);
            var jsonUsers = convertToJson(users);
            jsonUsers.forEach(out::print);
        }
        out.flush();
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
        var jsonUser = readJsonBody(request);
        var userDTO = convertFromJsonToDTOUser(jsonUser);
        if (isValidName(userDTO)) {
            service.updateById(id, userDTO);
            response.setStatus(SC_CREATED);
        } else {
            response.setStatus(SC_BAD_REQUEST);
        }
    }
}