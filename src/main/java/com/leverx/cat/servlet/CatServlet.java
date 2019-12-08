package com.leverx.cat.servlet;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;
import com.leverx.validator.ValidationFailedException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

import static com.leverx.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.printValidationErrorMessages;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class CatServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "cats";
    CatService catService = new CatServiceImpl();

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
        } catch (ValidationFailedException e) {
            printValidationErrorMessages(response, e.getMessage());
        }
    }

    public int printCatByIdToResponseBody(PrintWriter writer, String pathVariable) {
        try {
            var id = parseInt(pathVariable);
            var cat = catService.findById(id);
            var catJson = fromEntityToJson(cat);
            writer.print(catJson);
            return SC_OK;
        } catch (NoSuchElementException e) {
            return SC_NOT_FOUND;
        }

    }

    public int printAllCatsToResponseBody(PrintWriter writer) {
        var cats = catService.findAll();
        var catsJson = fromEntityCollectionToJson(cats);
        catsJson.forEach(writer::println);
        return cats.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }
}