package com.leverx.user.service;

import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;

import java.util.Collection;

public interface UserService {

    Collection<UserOutputDto> findAll();

    UserOutputDto findById(int id);

    Collection<UserOutputDto> findByName(String name);

    UserOutputDto save(UserInputDto user);

    void deleteById(String id);

    void updateById(String id, UserInputDto user);

    void pointsTransfer(int senderIdStr, int recipientId, int points);
}
