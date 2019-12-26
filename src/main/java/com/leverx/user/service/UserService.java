package com.leverx.user.service;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;

import java.util.Collection;

public interface UserService {

    Collection<UserOutputDto> findAll();

    UserOutputDto findById(int id) throws ElementNotFoundException;

    Collection<UserOutputDto> findByName(String name);

    UserOutputDto save(UserInputDto user) throws ValidationFailedException;

    void deleteById(String id);

    void updateById(String id, UserInputDto user) throws ValidationFailedException;

    void pointsTransfer(String senderIdStr, String recipientId, String points) throws ValidationFailedException;
}
