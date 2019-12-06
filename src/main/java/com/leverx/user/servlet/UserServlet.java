package com.leverx.user.servlet;

import com.leverx.user.dto.UserInputDto;
import com.leverx.user.facade.UserServletServiceAdapter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.leverx.utils.ServletUtils.initUserServletGetMethodType;
import static com.leverx.utils.ServletUtils.initUserServletPutMethodType;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static javax.servlet.http.HttpServletResponse.SC_OK;


public class UserServlet extends HttpServlet {

    private UserServletServiceAdapter adapter = new UserServletServiceAdapter();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var responseWriter = response.getWriter();
        var methodTypeWithPathVariable = initUserServletGetMethodType(request);
        var methodType = methodTypeWithPathVariable.getMethodType();
        var requiredVariable = methodTypeWithPathVariable.getPathVar();
        var responseStatus = SC_OK;
        switch (methodType) {
            case GET_ALL_USERS:
                responseStatus = adapter.printAllUsersToResponseBody(responseWriter);
                break;
            case GET_USER_BY_ID:
                responseStatus = adapter.printUserByIdToResponseBody(responseWriter, requiredVariable);
                break;
            case GET_USER_BY_NAME:
                responseStatus = adapter.printUsersByNameToResponseBody(responseWriter, requiredVariable);
                break;
            case GET_CATS_OF_USER:
                responseStatus = adapter.printCatsOfUser(responseWriter, requiredVariable);
                break;
            case GET_CAT_BY_ID_OF_USER:
                responseStatus = adapter.printUserCatById(responseWriter, requiredVariable);
                break;
        }
        responseWriter.flush();
        response.setStatus(responseStatus);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var userDto = readJsonBody(request, UserInputDto.class);
        adapter.performPost(response, userDto);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        adapter.performDelete(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var methodTypeWithPathVariable = initUserServletPutMethodType(request);
        var methodType = methodTypeWithPathVariable.getMethodType();
        var pathVariable = methodTypeWithPathVariable.getPathVar();
        switch (methodType) {
            case EDIT_USER: {
                adapter.editUser(request, response, pathVariable);
            }
            break;
            case ASSIGN_CATS_TO_USER: {
                adapter.assignCatsToUser(request, response, pathVariable);
            }
            break;
            case MOVE_CAT_TO_ANOTHER_USER:
                adapter.moveCatToAnotherUser(request, response, pathVariable);
        }
    }
}