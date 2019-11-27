package com.leverx.cat.servlet;

import com.leverx.cat.entity.CatDto;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.mapper.EntityJsonMapper.convertFromEntityCollectionToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromEntityToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromJsonToEntity;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.readBody;
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
        var jsonCatDto = readBody(request);
        var catDto = convertFromJsonToEntity(jsonCatDto, CatDto.class);
        try {
            catService.save(catDto);
            response.setStatus(SC_CREATED);
        } catch (IllegalArgumentException e) {
            response.setStatus(SC_BAD_REQUEST);
        }
    }

    private void printCatByIdToResponseBody(PrintWriter writer, String pathVariable) {
        var id = parseInt(pathVariable);
        var cat = catService.findById(id);
        var catJson = convertFromEntityToJson(cat);
        writer.print(catJson);
    }

    private void printAllCatsToResponseBody(PrintWriter writer) {
        var cats = catService.findAll();
        var catsJson = convertFromEntityCollectionToJson(cats);
        catsJson.forEach(writer::println);
    }
}
