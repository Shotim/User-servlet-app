package com.company.servlet;

import com.company.driver.MySQLDriver;
import com.company.repository.Repository;
import com.company.repository.RepositoryImpl;
import com.company.service.UserService;
import com.company.service.UserServiceImpl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Integer.parseInt;

@WebServlet(name = "userServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {

    private Gson gson = new Gson();

    private MySQLDriver driver = new MySQLDriver();

    private Repository repository = new RepositoryImpl(driver);

    private UserService service = new UserServiceImpl(gson, repository);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String name = request.getParameter("name");

        if (id != null) {
            out.print(service.findById(parseInt(id)));
        } else if (name != null) {
            service.findByName(name).forEach(out::print);
        } else {
            service.findAll().forEach(out::print);
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.addUser(request.getReader());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.deleteById(parseInt(request.getParameter("id")));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.updateById(request.getReader());
    }
}
