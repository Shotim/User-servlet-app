package com.leverx.model.pet.servlet;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.model.pet.service.PetService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.difactory.DIFactory.getBean;
import static com.leverx.utils.RequestURLUtils.getPathVariableFromRequest;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class PetServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "pets";
    private PetService petService = getBean(PetService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var responseWriter = response.getWriter();
        var pathVariable = getPathVariableFromRequest(request);
        var responseStatus = SC_OK;
        if (ORIGIN_PATH.equals(pathVariable)) {
            responseStatus = printAllPetsToResponseBody(responseWriter);
        } else {
            responseStatus = printPetByIdToResponseBody(responseWriter, pathVariable);
        }
        response.setStatus(responseStatus);
        responseWriter.flush();
    }

    private int printPetByIdToResponseBody(PrintWriter writer, String pathVariable) {
        try {
            var id = parseInt(pathVariable);
            var dog = petService.findById(id);
            var dogJson = fromEntityToJson(dog);
            writer.print(dogJson);
            return SC_OK;
        } catch (ElementNotFoundException e) {
            return e.getStatusCode();
        }
    }

    private int printAllPetsToResponseBody(PrintWriter writer) {
        var dog = petService.findAll();
        var dogsJson = fromEntityCollectionToJson(dog);
        dogsJson.forEach(writer::println);
        return dog.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }
}