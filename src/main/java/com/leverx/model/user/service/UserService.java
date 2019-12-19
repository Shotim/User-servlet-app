package com.leverx.model.user.service;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.exception.ValidationFailedException;
import com.leverx.model.user.dto.UserInputDto;
import com.leverx.model.user.dto.UserOutputDto;

import java.util.Collection;

public interface UserService {

    Collection<UserOutputDto> findAll();

    UserOutputDto findById(int id) throws ElementNotFoundException;

    Collection<UserOutputDto> findByName(String name);

    UserOutputDto save(UserInputDto user) throws ValidationFailedException;

    void deleteById(String id);

    void updateById(String id, UserInputDto user) throws ValidationFailedException;
}
