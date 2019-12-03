package com.leverx.user.service;

import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.user.entity.User;
import com.leverx.user.entity.UserInputDto;
import com.leverx.user.entity.UserOutputDto;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.leverx.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.leverx.utils.ServiceUtils.convertUserCollectionToUserOutputDtoCollection;
import static com.leverx.utils.ServiceUtils.convertUserInputDtoToUser;
import static com.leverx.utils.ServiceUtils.convertUserToUserOutputDto;
import static com.leverx.validator.EntityValidator.isValid;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepositoryImpl();
    private CatRepository catRepository = new CatRepositoryImpl();

    @Override
    public Collection<UserOutputDto> findAll() {
        Collection<User> users = userRepository.findAll();
        return convertUserCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto findById(int id) {
        var user = userRepository.findById(id);
        return convertUserToUserOutputDto(user);
    }

    @Override
    public Collection<UserOutputDto> findByName(String name) {
        var users = userRepository.findByName(name);
        return convertUserCollectionToUserOutputDtoCollection(users);
    }

    @Override
    public UserOutputDto save(UserInputDto userInputDto) {
        if (isValid(userInputDto)) {
            User user = convertUserInputDtoToUser(userInputDto);
            userRepository.save(user);
            return convertUserToUserOutputDto(user);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteById(String id) {
        if (isParsable(id)) {
            int parsedId = parseInt(id);
            userRepository.deleteById(parsedId);
        } else {
            log.debug("User was not updated. Check if id was correct");
        }
    }

    @Override
    public UserOutputDto updateById(String id, UserInputDto userInputDto) {
        var userId = parseInt(id);
        User user = ServiceUtils.convertUserInputDtoToUser(userId, userInputDto);
        if (isValid(userInputDto)) {
            userRepository.update(user);
            return convertUserToUserOutputDto(user);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void assignCatsToUser(int ownerId, List<Integer> catsIds) {
        var user = userRepository.findById(ownerId);
        var cats = catsIds.stream()
                .map(this::findCatIfExist)
                .filter(Objects::nonNull)
                .filter(cat -> cat.getOwner() == null)
                .collect(toSet());

        cats.addAll(user.getCats());
        user.setCats(cats);
        cats.forEach(cat -> cat.setOwner(user));
        userRepository.update(user);
    }

    private Cat findCatIfExist(Integer catId) {
        return catRepository.findById(catId);
    }
}