package com.leverx.user.servlet;

import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.user.entity.UserConstants.ID;
import static com.leverx.user.servlet.ServletUtils.getPathVariable;
import static com.leverx.user.servlet.ServletUtils.readJsonBody;
import static com.leverx.user.servlet.ServletUtils.variableIsANumber;
import static java.lang.Integer.parseInt;

@WebServlet(name = "userServlet", urlPatterns = {"/users", "/users/*"})
public class UserServlet extends HttpServlet {

    private static final String PATH = "users";
    private UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        var url = request.getRequestURL();
        var pathVariable = getPathVariable(url);
        if (PATH.equals(pathVariable)) {
            service.findAll()
                    .forEach(out::print);
        } else if (variableIsANumber(pathVariable)) {
            var id = parseInt(pathVariable);
            out.print(service.findById(id));
        } else {
            service.findByName(pathVariable)
                    .forEach(out::print);
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.save(readJsonBody(request));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        service.deleteById(request.getParameter(ID));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter(ID);
        String user = readJsonBody(request);
        service.updateById(id, user);
    }
}
