package com.leverx.user.service;

import com.leverx.user.entity.UserInputDto;
import com.leverx.user.entity.UserOutputDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    Collection<UserOutputDto> findAll();

    UserOutputDto findById(int id);

    Collection<UserOutputDto> findByName(String name);

    UserOutputDto save(UserInputDto user);

    void deleteById(String id);

    UserOutputDto updateById(String id, UserInputDto user);

    void assignCatsToUser(int ownerId, List<Integer> catsIds);
}
