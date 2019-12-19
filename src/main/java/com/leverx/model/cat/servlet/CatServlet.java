package com.leverx.model.cat.servlet;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.model.cat.dto.CatInputDto;
import com.leverx.model.cat.service.CatService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.difactory.DIFactory.getBean;
import static com.leverx.utils.RequestURLUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.printValidationErrorMessages;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class CatServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "cats";
    private CatService catService;

    public CatServlet() {
        catService = (CatService) getBean(CatService.class);
    }

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
        response.setStatus(responseStatus);
        responseWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var catInputDto = readJsonBody(request, CatInputDto.class);
        try {
            catService.save(catInputDto);
            response.setStatus(SC_CREATED);
        } catch (ValidationFailedException e) {
            printValidationErrorMessages(response, e);
        }
    }

    private int printCatByIdToResponseBody(PrintWriter writer, String pathVariable) {
        try {
            var id = parseInt(pathVariable);
            var cat = catService.findById(id);
            var catJson = fromEntityToJson(cat);
            writer.print(catJson);
            return SC_OK;
        } catch (ElementNotFoundException e) {
            return e.getStatusCode();
        }

    }

    private int printAllCatsToResponseBody(PrintWriter writer) {
        var cats = catService.findAll();
        var catsJson = fromEntityCollectionToJson(cats);
        catsJson.forEach(writer::println);
        return cats.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }
}