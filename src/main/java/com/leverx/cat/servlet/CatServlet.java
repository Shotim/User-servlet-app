package com.leverx.cat.servlet;

import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;
import com.leverx.utils.ServletUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.cat.mapper.CatJsonMapper.convertFromCatCollectionToJson;
import static com.leverx.cat.mapper.CatJsonMapper.convertFromCatToJson;
import static com.leverx.cat.mapper.CatJsonMapper.convertFromJsonToCatDto;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class CatServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "cats";
    private CatService catService = new CatServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var responseWriter = response.getWriter();
        var pathVariable = getPathVariableFromRequest(request);
        if (ORIGIN_PATH.equals(pathVariable)) {
            printAllCatsToResponseBody(responseWriter);
        } else {
            printCatByIdToResponseBody(responseWriter, pathVariable);
        }
        responseWriter.flush();
        response.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var jsonCatDto = ServletUtils.readBody(request);
        var catDto = convertFromJsonToCatDto(jsonCatDto);

        try {
            catService.save(catDto);
            response.setStatus(SC_CREATED);
        } catch (InternalServerErrorException e) {
            response.setStatus(SC_BAD_REQUEST);
        }
    }

    private void printCatByIdToResponseBody(PrintWriter writer, String pathVariable) {
        var id = parseInt(pathVariable);
        var cat = catService.findById(id);
        var catJson = convertFromCatToJson(cat);
        writer.print(catJson);
    }

    private void printAllCatsToResponseBody(PrintWriter writer) {
        var cats = catService.findAll();
        var catsJson = convertFromCatCollectionToJson(cats);
        catsJson.forEach(writer::println);
    }
}
