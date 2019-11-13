package com.leverx.user.servlet;

import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.user.servlet.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;

@WebServlet(name = "userServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {

    private static final String ID = "id";
    private static final String NAME = "name";

    private UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        String id = request.getParameter(ID);
        String name = request.getParameter(NAME);

        if (id != null) {
            out.print(service.findById(parseInt(id)));
        } else if (name != null) {
            service.findByName(name)
                    .forEach(out::print);
        } else {
            service.findAll()
                    .forEach(out::print);
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.save(readJsonBody(request));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.deleteById(request.getParameter(ID));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter(ID);
        String user = readJsonBody(request);
        service.updateById(id, user);
    }
}
