package com.leverx.user.service;

import com.leverx.exception.ElementNotFoundException;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.validator.ValidationFailedException;

import java.util.Collection;

public interface UserService {

    Collection<UserOutputDto> findAll();

    UserOutputDto findById(int id) throws ElementNotFoundException;

    Collection<UserOutputDto> findByName(String name);

    UserOutputDto save(UserInputDto user) throws ValidationFailedException;

    void deleteById(String id);

    UserOutputDto updateById(String id, UserInputDto user) throws ValidationFailedException;
}
