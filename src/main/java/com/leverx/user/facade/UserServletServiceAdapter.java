package com.leverx.user.facade;

import com.leverx.cat.dto.CatsDtoIdsList;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;
import com.leverx.user.dto.MoveUserId;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;
import com.leverx.validator.message.ValidationErrorsMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.leverx.cat.validator.CatValidator.validateCatsIds;
import static com.leverx.converter.EntityJsonConverter.fromEntityCollectionToJson;
import static com.leverx.converter.EntityJsonConverter.fromEntityToJson;
import static com.leverx.user.validator.UserValidator.validateUserId;
import static com.leverx.utils.ServletUtils.getPathVariableFromRequest;
import static com.leverx.utils.ServletUtils.printErrorMessages;
import static com.leverx.utils.ServletUtils.readJsonBody;
import static com.leverx.validator.EntityValidator.isValid;
import static java.lang.Integer.parseInt;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class UserServletServiceAdapter {

    private final UserService userService = new UserServiceImpl();
    private final CatService catService = new CatServiceImpl();

    public int printAllUsersToResponseBody(PrintWriter writer) {
        var users = userService.findAll();
        var jsonUsers = fromEntityCollectionToJson(users);
        jsonUsers.forEach(writer::println);
        return users.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }

    public int printUserByIdToResponseBody(PrintWriter writer, String pathVariable) {
        var id = parseInt(pathVariable);
        try {
            var user = userService.findById(id);
            var jsonUser = fromEntityToJson(user);
            writer.print(jsonUser);
            return SC_OK;
        } catch (NoSuchElementException e) {
            return SC_NOT_FOUND;
        }
    }

    public int printUsersByNameToResponseBody(PrintWriter writer, String pathVariable) {
        var users = userService.findByName(pathVariable);
        var jsonUsers = fromEntityCollectionToJson(users);
        jsonUsers.forEach(writer::println);
        return users.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }

    public int printCatsOfUser(PrintWriter writer, String ownerId) {
        var id = parseInt(ownerId);
        var cats = catService.findByOwner(id);
        var jsonCats = fromEntityCollectionToJson(cats);
        jsonCats.forEach(writer::println);
        return cats.size() != 0 ? SC_OK : SC_NOT_FOUND;
    }

    public int printUserCatById(PrintWriter writer, String catId) {
        var id = parseInt(catId);
        try {
            var cat = catService.findById(id);
            var jsonCat = fromEntityToJson(cat);
            writer.print(jsonCat);
            return SC_OK;
        } catch (NoSuchElementException e) {
            return SC_NOT_FOUND;
        }
    }

    public void performPost(HttpServletResponse response, UserInputDto userDto) throws IOException {
        var optionalValid = validate(userDto);
        if (optionalValid.isPresent()) {
            printErrorMessages(response, optionalValid.get());
        } else {
            userService.save(userDto);
            response.setStatus(SC_CREATED);
        }
    }

    public void performDelete(HttpServletRequest request, HttpServletResponse response) {
        var id = getPathVariableFromRequest(request);
        userService.deleteById(id);
        response.setStatus(SC_NO_CONTENT);
    }

    public void editUser(HttpServletRequest request, HttpServletResponse response, String pathVariable) throws IOException {
        var userDto = readJsonBody(request, UserInputDto.class);
        var optionalValid = validate(userDto);
        if (optionalValid.isPresent()) {
            printErrorMessages(response, optionalValid.get());
        } else {
            userService.updateById(pathVariable, userDto);
            response.setStatus(SC_OK);
        }
    }

    public void moveCatToAnotherUser(HttpServletRequest request, HttpServletResponse response, String pathVariable) throws IOException {
        var moveUserId = readJsonBody(request, MoveUserId.class);
        var userId = moveUserId.getId();
        var optionalValid = validateUserId(userId);
        if (optionalValid.isPresent()) {
            printErrorMessages(response, optionalValid.get());
            response.setStatus(SC_BAD_REQUEST);
        } else {
            var catId = parseInt(pathVariable);
            catService.update(catId, userId);
        }
    }

    public void assignCatsToUser(HttpServletRequest request, HttpServletResponse response, String pathVariable) throws IOException {
        var catsIds = readJsonBody(request, CatsDtoIdsList.class);
        var catsIdsList = catsIds.getIds();
        var optionalValid = validateCatsIds(catsIdsList, null);
        if (optionalValid.isPresent()) {
            printErrorMessages(response, optionalValid.get());
            response.setStatus(422);
        } else {
            var ownerId = parseInt(pathVariable);
            catService.assignCatsToExistingUser(ownerId, catsIdsList);
            response.setStatus(SC_OK);
        }
    }

    private Optional<ValidationErrorsMessage> validate(UserInputDto userDto) {
        var optionalValid = isValid(userDto);
        optionalValid = validateCatsIds(userDto.getCatsIdsList(), optionalValid.orElseThrow());
        return optionalValid;
    }
}