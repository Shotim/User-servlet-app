package com.leverx.factory.beanFactory;

import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
import com.leverx.cat.service.CatService;
import com.leverx.cat.service.CatServiceImpl;
import com.leverx.cat.validator.CatValidator;
import com.leverx.dog.repository.DogRepository;
import com.leverx.dog.repository.DogRepositoryImpl;
import com.leverx.dog.service.DogService;
import com.leverx.dog.service.DogServiceImpl;
import com.leverx.dog.validator.DogValidator;
import com.leverx.pet.repository.PetRepository;
import com.leverx.pet.repository.PetRepositoryImpl;
import com.leverx.pet.service.PetService;
import com.leverx.pet.service.PetServiceImpl;
import com.leverx.user.dto.converter.UserDtoConverter;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.repository.UserRepositoryImpl;
import com.leverx.user.service.UserService;
import com.leverx.user.service.UserServiceImpl;
import com.leverx.user.validator.UserValidator;
import com.leverx.validator.EntityValidator;

public class BeanFactory {

    private static UserRepository userRepository = new UserRepositoryImpl();
    private static PetRepository petRepository = new PetRepositoryImpl();
    private static CatRepository catRepository = new CatRepositoryImpl();
    private static DogRepository dogRepository = new DogRepositoryImpl();
    private static EntityValidator entityValidator = new EntityValidator();
    private static UserDtoConverter userDtoConverter = new UserDtoConverter(catRepository, dogRepository);
    private static CatValidator catValidator = new CatValidator(catRepository);
    private static DogValidator dogValidator = new DogValidator(dogRepository);
    private static UserValidator userValidator = new UserValidator(entityValidator, userRepository, catValidator, dogValidator);
    private static PetService petService = new PetServiceImpl(petRepository);
    private static DogService dogService = new DogServiceImpl(entityValidator, dogRepository);
    private static CatService catService = new CatServiceImpl(entityValidator, catRepository);
    private static UserService userService = new UserServiceImpl(userValidator, userRepository, userDtoConverter);

    public static PetService getPetService() {
        return petService;
    }

    public static DogService getDogService() {
        return dogService;
    }

    public static CatService getCatService() {
        return catService;
    }

    public static UserService getUserService() {
        return userService;
    }

}
