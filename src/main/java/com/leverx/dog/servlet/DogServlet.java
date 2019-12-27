package com.leverx.dog.servlet;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.service.DogService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.leverx.core.config.BeanFactory.getDogService;
import static com.leverx.core.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.core.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.core.utils.RequestURLUtils.getPathVariableFromRequest;
import static com.leverx.core.utils.ServletUtils.printValidationErrorMessages;
import static com.leverx.core.utils.ServletUtils.readJsonBody;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class DogServlet extends HttpServlet {

    private static final String ORIGIN_PATH = "dogs";
    private DogService dogService;

    public DogServlet() {
        this.dogService = getDogService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var responseWriter = response.getWriter();
        var pathVariable = getPathVariableFromRequest(request);
        var responseStatus = SC_OK;
        if (ORIGIN_PATH.equals(pathVariable)) {
            responseStatus = printAllDogsToResponseBody(responseWriter);
        } else {
            responseStatus = printDogByIdToResponseBody(responseWriter, pathVariable);
        }
        response.setStatus(responseStatus);
        responseWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var dogInputDto = readJsonBody(request, DogInputDto.class);
        try {
            dogService.save(dogInputDto);
            response.setStatus(SC_CREATED);
        } catch (ValidationFailedException e) {
            printValidationErrorMessages(response, e);
        }
    }

    private int printDogByIdToResponseBody(PrintWriter writer, String pathVariable) {
        try {
            var id = parseInt(pathVariable);
            var dog = dogService.findById(id);
            var dogJson = fromEntityToJson(dog);
            writer.print(dogJson);
            return SC_OK;
        } catch (ElementNotFoundException e) {
            return e.getStatusCode();
        }

    }

    private int printAllDogsToResponseBody(PrintWriter writer) {
        var dog = dogService.findAll();
        var dogsJson = fromEntityCollectionToJson(dog);
        dogsJson.forEach(writer::println);
        return dog.isEmpty() ? SC_NOT_FOUND : SC_OK;
    }
}
