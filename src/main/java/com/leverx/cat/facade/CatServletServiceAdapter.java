package com.leverx.cat.facade;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

import static com.leverx.mapper.EntityJsonMapper.convertFromEntityCollectionToJson;
import static com.leverx.mapper.EntityJsonMapper.convertFromEntityToJson;
import static com.leverx.utils.ServletUtils.printErrorMessages;
import static com.leverx.validator.EntityValidator.isValid;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class CatServletServiceAdapter {

    CatService catService = new CatServiceImpl();

    public int printCatByIdToResponseBody(PrintWriter writer, String pathVariable) {
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

    public int printAllCatsToResponseBody(PrintWriter writer) {
        var cats = catService.findAll();
        var catsJson = convertFromEntityCollectionToJson(cats);
        catsJson.forEach(writer::println);
        return cats.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }

    public void performPost(HttpServletResponse response, CatInputDto catInputDto) throws IOException {
        var optionalValid = isValid(catInputDto);

        if (optionalValid.isPresent()) {
            printErrorMessages(response, optionalValid.get());
        } else {
            catService.save(catInputDto);
            response.setStatus(SC_CREATED);
        }
    }
}