package com.leverx.cat.servlet;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

import static com.leverx.mapper.EntityJsonMapper.convertFromEntityCollectionToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromEntityToJson;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class CatServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "cats";
    private CatService catService = new CatServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var responseWriter = response.getWriter();
        var pathVariable = getPathVariableFromRequest(request);
        var responseStatus = SC_OK;
        if (ORIGIN_PATH.equals(pathVariable)) {
            responseStatus = printAllCatsToResponseBody(responseWriter);
        } else {
            responseStatus = printCatByIdToResponseBody(responseWriter, pathVariable);
        }
        responseWriter.flush();
        response.setStatus(responseStatus);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var catInputDto = readJsonBody(request, CatInputDto.class);
        try {
            catService.save(catInputDto);
            response.setStatus(SC_CREATED);
        } catch (IllegalArgumentException e) {
            response.setStatus(SC_BAD_REQUEST);
        }
    }

    private int printCatByIdToResponseBody(PrintWriter writer, String pathVariable) {
        try {
            var id = parseInt(pathVariable);
            var cat = catService.findById(id);
            var catJson = convertFromEntityToJson(cat);
            writer.print(catJson);
            return SC_OK;
        } catch (NoSuchElementException e) {
            return SC_NOT_FOUND;
        }

    }

    private int printAllCatsToResponseBody(PrintWriter writer) {
        var cats = catService.findAll();
        var catsJson = convertFromEntityCollectionToJson(cats);
        catsJson.forEach(writer::println);
        return cats.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }
}
