package com.leverx.cat.servlet;

import com.leverx.cat.facade.CatServletServiceAdapter;
import com.leverx.cat.dto.CatInputDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class CatServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "cats";
    private final CatServletServiceAdapter adapter = new CatServletServiceAdapter();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var responseWriter = response.getWriter();
        var pathVariable = getPathVariableFromRequest(request);
        var responseStatus = SC_OK;
        if (ORIGIN_PATH.equals(pathVariable)) {
            responseStatus = adapter.printAllCatsToResponseBody(responseWriter);
        } else {
            responseStatus = adapter.printCatByIdToResponseBody(responseWriter, pathVariable);
        }
        responseWriter.flush();
        response.setStatus(responseStatus);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var catInputDto = readJsonBody(request, CatInputDto.class);
        adapter.performPost(response, catInputDto);
    }
}
