package com.leverx.user.service;

import com.leverx.cat.entity.Cat;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.dog.entity.Dog;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.entity.Pet;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.dto.converter.UserDtoConverter;
import com.leverx.user.entity.User;
import com.leverx.user.repository.UserRepository;
import com.leverx.user.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.leverx.core.config.BeanFactory.getUserService;
import static com.leverx.core.config.HibernateEMFConfig.getEntityManagerFactory;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UserServiceTest {

    @InjectMocks
    private UserService userService = getUserService();

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserDtoConverter mockUserDtoConverter;

    @Mock
    private UserValidator mockUserValidator;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void findAll_ShouldReturnUserList() {

        //Given
        var id = 1;
        var name = "Name";
        var email = "qwer@qwer";
        var animalPoints = 100;
        var user = new User();
        user.setId(id);
        user.setAnimalPoints(animalPoints);
        user.setEmail(email);
        user.setName(name);
        var userList = List.of(user);

        var dogId = 3;
        var dogDateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var dogName = "petya";
        var dog = new Dog();
        dog.setId(dogId);
        dog.setCutEars(isCutEars);
        dog.setDateOfBirth(dogDateOfBirth);
        dog.setName(dogName);
        dog.setOwners(userList);

        var catId = 2;
        var catDateOfBirth = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber = 7;
        var catName = "petya";
        var cat = new Cat();
        cat.setId(catId);
        cat.setName(catName);
        cat.setDateOfBirth(catDateOfBirth);
        cat.setMiceCaughtNumber(miceCaughtNumber);
        cat.setOwners(userList);

        var expectedPetCollection = List.of(cat, dog);
        user.setPets(expectedPetCollection);
        when(mockUserRepository.findAll()).thenReturn(userList);

        var catOutputDto = new PetOutputDto(catId, catName, catDateOfBirth, List.of(user.getId()));
        var dogOutputDto = new PetOutputDto(dogId, dogName, dogDateOfBirth, List.of(user.getId()));
        var petOutputDtoList = List.of(catOutputDto, dogOutputDto);
        var userOutputDto = new UserOutputDto(id, name, email, animalPoints, petOutputDtoList);
        final List<UserOutputDto> expectedUserOutputDtoCollection = List.of(userOutputDto);
        when(mockUserDtoConverter.userCollectionToUserOutputDtoCollection(userList)).thenReturn(expectedUserOutputDtoCollection);

        //When
        var actualUserOutputDtoCollection = userService.findAll();

        //Then
        assertEquals(expectedUserOutputDtoCollection, actualUserOutputDtoCollection);
    }

    @Test
    void findAll_ShouldReturnEmptyList() {

        //Given
        when(mockUserRepository.findAll()).thenReturn(emptyList());

        //When
        var actualUserList = userService.findAll();

        //Then
        assertTrue(actualUserList.isEmpty());
    }

    @Test
    void findById_GivenExistingId_ShouldReturnExistingUser() {

        //Given
        var id = 1;
        var name = "Name";
        var email = "qwer@qwer";
        var animalPoints = 100;
        var user = new User();
        user.setId(id);
        user.setAnimalPoints(animalPoints);
        user.setEmail(email);
        user.setName(name);
        var userList = List.of(user);

        var dogId = 3;
        var dogDateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var dogName = "petya";
        var dog = new Dog();
        dog.setId(dogId);
        dog.setCutEars(isCutEars);
        dog.setDateOfBirth(dogDateOfBirth);
        dog.setName(dogName);
        dog.setOwners(userList);

        var catId = 2;
        var catDateOfBirth = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber = 7;
        var catName = "petya";
        var cat = new Cat();
        cat.setId(catId);
        cat.setName(catName);
        cat.setDateOfBirth(catDateOfBirth);
        cat.setMiceCaughtNumber(miceCaughtNumber);
        cat.setOwners(userList);

        user.setPets(List.of(cat, dog));

        var catOutputDto = new PetOutputDto(catId, catName, catDateOfBirth, List.of(user.getId()));
        var dogOutputDto = new PetOutputDto(dogId, dogName, dogDateOfBirth, List.of(user.getId()));
        var petOutputDtoList = List.of(catOutputDto, dogOutputDto);
        var expectedUserOutputDto = new UserOutputDto(id, name, email, animalPoints, petOutputDtoList);
        when(mockUserRepository.findById(id)).thenReturn(Optional.of(user));
        when(mockUserDtoConverter.userToUserOutputDto(user)).thenReturn(expectedUserOutputDto);

        //When
        var actualUserOutputDto = userService.findById(id);

        //Then
        assertEquals(expectedUserOutputDto, actualUserOutputDto);

    }

    @Test
    void findById_GivenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockUserRepository.findById(anyInt())).thenThrow(ElementNotFoundException.class);

        //When
        Executable whenStatement = () -> userService.findById(anyInt());

        //Then
        assertThrows(ElementNotFoundException.class, whenStatement);
    }

    @Test
    void findByName_GivenExistingName_ShouldReturnExistingPets() {

        //Given
        var id = 1;
        var name = "Name";
        var email = "qwer@qwer";
        var animalPoints = 100;
        var user = new User();
        user.setId(id);
        user.setAnimalPoints(animalPoints);
        user.setEmail(email);
        user.setName(name);
        var userList = List.of(user);

        var dogId = 3;
        var dogDateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var dogName = "petya";
        var dog = new Dog();
        dog.setId(dogId);
        dog.setCutEars(isCutEars);
        dog.setDateOfBirth(dogDateOfBirth);
        dog.setName(dogName);
        dog.setOwners(userList);

        var catId = 2;
        var catDateOfBirth = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber = 7;
        var catName = "petya";
        var cat = new Cat();
        cat.setId(catId);
        cat.setName(catName);
        cat.setDateOfBirth(catDateOfBirth);
        cat.setMiceCaughtNumber(miceCaughtNumber);
        cat.setOwners(userList);

        var expectedPetCollection = List.of(cat, dog);
        user.setPets(expectedPetCollection);
        when(mockUserRepository.findByName(name)).thenReturn(userList);

        var catOutputDto = new PetOutputDto(catId, catName, catDateOfBirth, List.of(user.getId()));
        var dogOutputDto = new PetOutputDto(dogId, dogName, dogDateOfBirth, List.of(user.getId()));
        var petOutputDtoList = List.of(catOutputDto, dogOutputDto);
        var userOutputDto = new UserOutputDto(id, name, email, animalPoints, petOutputDtoList);
        final List<UserOutputDto> expectedUserOutputDtoCollection = List.of(userOutputDto);
        when(mockUserDtoConverter.userCollectionToUserOutputDtoCollection(userList)).thenReturn(expectedUserOutputDtoCollection);

        //When
        var actualUserOutputDtoCollection = userService.findByName(name);

        //Then
        assertEquals(expectedUserOutputDtoCollection, actualUserOutputDtoCollection);
    }

    @Test
    void findByName_GivenExistingName_ShouldReturnEmptyList() {

        //Given
        when(mockUserRepository.findByName(anyString())).thenReturn(emptyList());

        //When
        var actualUserCollection = userService.findByName(anyString());

        //Then
        assertTrue(actualUserCollection.isEmpty());
    }

    @Test
    void save_GivenCorrectUserInputDto_ShouldSaveItAndReturnUserOutputDto() {

        //Given
        var name = "Nowqwe";
        var email = "qwert@yrty";
        var animalPoints = 78;
        var catsIds = List.of(1);
        var dogsIds = List.of(2);

        var id = 3;
        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var petId1 = 1;
        var petName1 = "vasya";
        var petDB1 = LocalDate.of(2019, 12, 1);
        var petOwnerIds1 = List.of(1, 2, 3);
        var petOutputDto1 = new PetOutputDto(petId1, petName1, petDB1, petOwnerIds1);
        var pet1 = new Cat();
        pet1.setId(petId1);
        pet1.setName(petName1);
        pet1.setDateOfBirth(petDB1);

        var petId2 = 2;
        var petName2 = "petya";
        var petDB2 = LocalDate.of(2019, 12, 1);
        var petOwnerIds2 = List.of(1, 2, 3);
        var petOutputDto2 = new PetOutputDto(petId2, petName2, petDB2, petOwnerIds2);
        var pet2 = new Dog();
        pet2.setId(petId2);
        pet2.setName(petName2);
        pet2.setDateOfBirth(petDB2);

        var expectedUserOutputDto = new UserOutputDto(id, name, email, animalPoints, List.of(petOutputDto1, petOutputDto2));

        var user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setAnimalPoints(animalPoints);
        user.setPets(List.of(pet1, pet2));
        pet1.setOwners(List.of(user));
        pet2.setOwners(List.of(user));

        doNothing().when(mockUserValidator).validateCreateUser(userInputDto);
        when(mockUserDtoConverter.userInputDtoToUser(userInputDto)).thenReturn(user);
        when(mockUserRepository.save(user)).thenReturn(Optional.of(user));
        when(mockUserDtoConverter.userToUserOutputDto(user)).thenReturn(expectedUserOutputDto);
        //When
        var actualUserOutputDto = userService.save(userInputDto);
        //Then
        assertEquals(expectedUserOutputDto, actualUserOutputDto);
        verify(mockUserDtoConverter, times(1)).userInputDtoToUser(userInputDto);
        verify(mockUserDtoConverter, times(1)).userToUserOutputDto(user);
        verify(mockUserRepository, times(1)).save(user);
        verify(mockUserDtoConverter).userInputDtoToUser(userInputDto);
        verify(mockUserDtoConverter).userToUserOutputDto(user);
        verify(mockUserRepository).save(user);

    }

    @Test
    void save_GivenIncorrectUserInputDto_ShouldThrowValidationFailedException() {

        //Given
        var name = "Now";
        var email = "qwerrty";
        var animalPoints = -1;
        var catsIds = List.of(2);
        var dogsIds = List.of(1);

        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var expectedErrors = "\"Name 'Now' should be between 5 and 60 symbols; Number '-1' must be positive or zero; " +
                "This email is not valid; 2: Cat with this id does not exist in database;" +
                " 1: Dog with this id does not exist in database\"";
        doThrow(new ValidationFailedException(expectedErrors)).when(mockUserValidator).validateCreateUser(userInputDto);
        //Then
        assertThrows(ValidationFailedException.class, () -> userService.save(userInputDto));
        verify(mockUserValidator, times(1)).validateCreateUser(userInputDto);
        verify(mockUserValidator).validateCreateUser(userInputDto);
        verifyNoInteractions(mockUserDtoConverter, mockUserRepository);

    }

    @Test
    void deleteId_ShouldDoNothing() {

        //Given
        var idInt = 3;
        var idStr = "3";
        doNothing().when(mockUserRepository).deleteById(idInt);
        //When
        userService.deleteById(idStr);
        //Then
        verify(mockUserRepository, times(1)).deleteById(idInt);
        verify(mockUserRepository).deleteById(idInt);

    }

    @Test
    void updateById_GivenCorrectUserInputDto_ShouldUpdateIt() {

        //Given
        var id = 3;
        var name = "Nowqwe";
        var email = "qwert@yrty";
        var animalPoints = 78;
        var catsIds = List.of(1);
        var dogsIds = List.of(2);

        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var petId1 = 1;
        var petName1 = "vasya";
        var petDB1 = LocalDate.of(2019, 12, 1);
        var pet1 = new Cat();
        pet1.setId(petId1);
        pet1.setName(petName1);
        pet1.setDateOfBirth(petDB1);

        var petId2 = 2;
        var petName2 = "petya";
        var petDB2 = LocalDate.of(2019, 12, 1);
        var pet2 = new Dog();
        pet2.setId(petId2);
        pet2.setName(petName2);
        pet2.setDateOfBirth(petDB2);

        var user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setAnimalPoints(animalPoints);
        user.setPets(new ArrayList<>(List.of(pet1, pet2)));
        pet1.setOwners(new ArrayList<>(List.of(user)));
        pet2.setOwners(new ArrayList<>(List.of(user)));

        doNothing().when(mockUserValidator).validateUpdateUser(id, userInputDto);
        when(mockUserDtoConverter.userInputDtoToUser(id, userInputDto)).thenReturn(user);
        doNothing().when(mockUserRepository).update(user);
        //When
        userService.updateById(Integer.toString(id), userInputDto);
        //Then
        verify(mockUserValidator, times(1)).validateUpdateUser(id, userInputDto);
        verify(mockUserDtoConverter, times(1)).userInputDtoToUser(id, userInputDto);
        verify(mockUserRepository, times(1)).update(user);
        verify(mockUserValidator).validateUpdateUser(id, userInputDto);
        verify(mockUserDtoConverter).userInputDtoToUser(id, userInputDto);
        verify(mockUserRepository).update(user);

    }

    @Test
    void updateById_GivenIncorrectUserInputDto_ShouldThrowValidationFailedException() {

        //Given
        var idInt = 3;
        var idStr = "3";
        var name = "No";
        var email = "qwerty";
        var animalPoints = -78;
        var catsIds = List.of(2);
        var dogsIds = List.of(1);

        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var expectedErrors = "\"Name 'No' should be between 5 and 60 symbols; " +
                "This email is not valid; " +
                "Number '-78' must be positive or zero; " +
                "2: Cat with this id does not exist in database; " +
                "1: Dog with this id does not exist in database\"";
        var validationFailedException = new ValidationFailedException(expectedErrors);
        doThrow(validationFailedException).when(mockUserValidator).validateUpdateUser(idInt, userInputDto);
        //Then
        assertThrows(ValidationFailedException.class, () -> userService.updateById(idStr, userInputDto));
        verify(mockUserValidator, times(1)).validateUpdateUser(idInt, userInputDto);
        verify(mockUserValidator).validateUpdateUser(idInt, userInputDto);
        verifyNoInteractions(mockUserRepository, mockUserDtoConverter);
    }

    @Test
    void pointsTransfer_GivenCorrectData_ShouldExecuteTransfer() {

        //Given
        getEntityManagerFactory();
        var senderId = 3;
        var name1 = "Name";
        var email1 = "qwe@rty";
        var animalPoints1 = 78;
        var pets1 = new ArrayList<Pet>(emptyList());

        var sender = new User();
        sender.setId(senderId);
        sender.setName(name1);
        sender.setEmail(email1);
        sender.setAnimalPoints(animalPoints1);
        sender.setPets(pets1);

        var recipientId = 4;
        var name2 = "Name";
        var email2 = "qwe@rty";
        var animalPoints2 = 78;
        var pets2 = new ArrayList<Pet>(emptyList());

        var recipient = new User();
        recipient.setId(recipientId);
        recipient.setName(name2);
        recipient.setEmail(email2);
        recipient.setAnimalPoints(animalPoints2);
        recipient.setPets(pets2);

        var transferredAnimalPoints = 30;

        doNothing().when(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        when(mockUserRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(mockUserRepository.findById(recipientId)).thenReturn(Optional.of(recipient));

        sender.setAnimalPoints(sender.getAnimalPoints() - transferredAnimalPoints);
        recipient.setAnimalPoints(recipient.getAnimalPoints() + transferredAnimalPoints);
        doNothing().when(mockUserRepository).update(sender);
        doNothing().when(mockUserRepository).update(recipient);
        //When
        userService.pointsTransfer(senderId, recipientId, transferredAnimalPoints);
        //Then
        verify(mockUserRepository, times(2)).findById(anyInt());
        verify(mockUserRepository, times(2)).update(any(User.class));
        verify(mockUserValidator, times(1)).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        verify(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
    }

    @Test
    void pointsTransfer_GivenEqualSenderAndRecipientId_ShouldThrowValidationFailedException() {

        //Given
        getEntityManagerFactory();
        var senderId = 3;
        var recipientId = 3;

        var name1 = "Name";
        var email1 = "qwe@rty";
        var animalPoints1 = 78;
        var pets1 = new ArrayList<Pet>(emptyList());

        var user = new User();
        user.setId(senderId);
        user.setName(name1);
        user.setEmail(email1);
        user.setAnimalPoints(animalPoints1);
        user.setPets(pets1);

        var transferredAnimalPoints = 30;
        var errorMessage = "\"You can't transfer points to yourself; Not enough money\"";
        var validationFailedException = new ValidationFailedException(errorMessage);
        when(mockUserRepository.findById(senderId)).thenReturn(Optional.of(user));
        when(mockUserRepository.findById(recipientId)).thenReturn(Optional.of(user));
        doThrow(validationFailedException).when(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        //Then
        assertThrows(ValidationFailedException.class, () -> userService.pointsTransfer(senderId, recipientId, transferredAnimalPoints));
        verify(mockUserValidator, times(1)).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        verify(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);

    }

    @Test
    void pointsTransfer_GivenMoreMoneyThanSenderHas_ShouldThrowValidationFailedException() {

        //Given
        getEntityManagerFactory();
        var senderId = 3;
        var name1 = "Name";
        var email1 = "qwe@rty";
        var animalPoints1 = 78;
        var pets1 = new ArrayList<Pet>(emptyList());

        var sender = new User();
        sender.setId(senderId);
        sender.setName(name1);
        sender.setEmail(email1);
        sender.setAnimalPoints(animalPoints1);
        sender.setPets(pets1);

        var recipientId = 4;
        var name2 = "Name";
        var email2 = "qwe@rty";
        var animalPoints2 = 78;
        var pets2 = new ArrayList<Pet>(emptyList());

        var recipient = new User();
        recipient.setId(recipientId);
        recipient.setName(name2);
        recipient.setEmail(email2);
        recipient.setAnimalPoints(animalPoints2);
        recipient.setPets(pets2);
        var transferredAnimalPoints = 300;
        var errorMessage = "\"Not enough money\"";
        var validationFailedException = new ValidationFailedException(errorMessage);
        when(mockUserRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(mockUserRepository.findById(recipientId)).thenReturn(Optional.of(recipient));
        doThrow(validationFailedException).when(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        //Then
        assertThrows(ValidationFailedException.class, () -> userService.pointsTransfer(senderId, recipientId, transferredAnimalPoints));
        verify(mockUserValidator, times(1)).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        verify(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
    }

    @Test
    void pointsTransfer_GivenIncorrectSenderId_ShouldThrowValidationFailedException() {

        //Given
        getEntityManagerFactory();
        var senderId = 11;
        var recipientId = 5;
        var transferredAnimalPoints = 30;
        var errorMessage = "\"11: User with this id was not found\"";
        var validationFailedException = new ValidationFailedException(errorMessage);
        when(mockUserRepository.findById(senderId)).thenThrow(validationFailedException);
        doNothing().when(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        //Then
        assertThrows(ValidationFailedException.class, () -> userService.pointsTransfer(senderId, recipientId, transferredAnimalPoints));
        verify(mockUserRepository, times(1)).findById(senderId);
        verify(mockUserRepository).findById(senderId);
        verifyNoInteractions(mockUserValidator, mockUserDtoConverter);
    }

    @Test
    void pointsTransfer_GivenIncorrectRecipientId_ShouldThrowValidationFailedException() {

        //Given
        getEntityManagerFactory();
        var senderId = 5;
        var name1 = "Name";
        var email1 = "qwe@rty";
        var animalPoints1 = 78;
        var pets1 = new ArrayList<Pet>(emptyList());

        var sender = new User();
        sender.setId(senderId);
        sender.setName(name1);
        sender.setEmail(email1);
        sender.setAnimalPoints(animalPoints1);
        sender.setPets(pets1);

        var recipientId = 11;
        var transferredAnimalPoints = 30;
        var errorMessage = "\"11: User with this id was not found\"";
        var validationFailedException = new ValidationFailedException(errorMessage);
        doNothing().when(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        when(mockUserRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(mockUserRepository.findById(recipientId)).thenThrow(validationFailedException);
        //Then
        assertThrows(ValidationFailedException.class, () -> userService.pointsTransfer(senderId, recipientId, transferredAnimalPoints));
        verify(mockUserRepository, times(1)).findById(recipientId);
        verify(mockUserRepository).findById(recipientId);
        verifyNoInteractions(mockUserValidator, mockUserDtoConverter);
    }
}