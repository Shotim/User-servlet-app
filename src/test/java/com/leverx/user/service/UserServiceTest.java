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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.leverx.core.config.BeanFactory.getUserService;
import static com.leverx.core.config.HibernateEMFConfig.getEntityManagerFactory;
import static com.leverx.core.utils.TestUtils.cutEars;
import static com.leverx.core.utils.TestUtils.id;
import static com.leverx.core.utils.TestUtils.invalidAnimalPoints;
import static com.leverx.core.utils.TestUtils.invalidEmail;
import static com.leverx.core.utils.TestUtils.invalidName;
import static com.leverx.core.utils.TestUtils.validAnimalPoints;
import static com.leverx.core.utils.TestUtils.validDateOfBirth;
import static com.leverx.core.utils.TestUtils.validEmail;
import static com.leverx.core.utils.TestUtils.validMiceCaughtNumber;
import static com.leverx.core.utils.TestUtils.validName;
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
    void findAll_GivenExpectedData_ShouldReturnUserList() {

        //Given
        var id = id();
        var name = validName();
        var email = validEmail();
        var animalPoints = validAnimalPoints();
        var dogId = id();
        var dogDateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var dogName = validName();
        var catId = id();
        var catDateOfBirth = validDateOfBirth();
        var miceCaughtNumber = validMiceCaughtNumber();
        var catName = validName();

        var cat = new Cat();
        var dog = new Dog();

        dog.setId(dogId);
        dog.setCutEars(isCutEars);
        dog.setDateOfBirth(dogDateOfBirth);
        dog.setName(dogName);

        cat.setId(catId);
        cat.setName(catName);
        cat.setDateOfBirth(catDateOfBirth);
        cat.setMiceCaughtNumber(miceCaughtNumber);
        var petCollection = List.of(cat, dog);

        var user = new User(id, name, email, animalPoints, petCollection);
        var userList = List.of(user);

        dog.setOwners(userList);
        cat.setOwners(userList);


        user.setPets(petCollection);
        when(mockUserRepository.findAll()).thenReturn(userList);

        var catOutputDto = new PetOutputDto(catId, catName, catDateOfBirth, List.of(user.getId()));
        var dogOutputDto = new PetOutputDto(dogId, dogName, dogDateOfBirth, List.of(user.getId()));
        var petOutputDtoList = newArrayList(catOutputDto, dogOutputDto);
        var userOutputDto = new UserOutputDto(id, name, email, animalPoints, petOutputDtoList);
        final List<UserOutputDto> expectedUserOutputDtoCollection = List.of(userOutputDto);
        when(mockUserDtoConverter.userCollectionToUserOutputDtoCollection(userList)).thenReturn(expectedUserOutputDtoCollection);

        //When
        var actualUserOutputDtoCollection = userService.findAll();

        //Then
        assertEquals(expectedUserOutputDtoCollection, actualUserOutputDtoCollection);
    }

    @Test
    void findById_GivenExistingId_ShouldReturnExistingUser() {

        //Given
        var id = id();
        var name = validName();
        var email = validEmail();
        var animalPoints = validAnimalPoints();
        var dogId = id();
        var dogDateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var dogName = validName();
        var catId = id();
        var catDateOfBirth = validDateOfBirth();
        var miceCaughtNumber = validMiceCaughtNumber();
        var catName = validName();

        var cat = new Cat();
        var dog = new Dog();

        var petCollection = newArrayList(cat, dog);
        var user = new User(id, name, email, animalPoints, petCollection);
        var userList = List.of(user);

        dog.setId(dogId);
        dog.setCutEars(isCutEars);
        dog.setDateOfBirth(dogDateOfBirth);
        dog.setName(dogName);
        dog.setOwners(userList);

        cat.setId(catId);
        cat.setName(catName);
        cat.setDateOfBirth(catDateOfBirth);
        cat.setMiceCaughtNumber(miceCaughtNumber);
        cat.setOwners(userList);

        user.setPets(petCollection);

        var catOutputDto = new PetOutputDto(catId, catName, catDateOfBirth, List.of(user.getId()));
        var dogOutputDto = new PetOutputDto(dogId, dogName, dogDateOfBirth, List.of(user.getId()));
        var petOutputDtoList = newArrayList(catOutputDto, dogOutputDto);
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
        var id = id();
        var name = validName();
        var email = validEmail();
        var animalPoints = validAnimalPoints();

        var dogId = id();
        var dogDateOfBirth = validDateOfBirth();
        var isCutEars = cutEars();
        var dogName = validName();

        var catId = id();
        var catDateOfBirth = validDateOfBirth();
        var miceCaughtNumber = validMiceCaughtNumber();
        var catName = validName();

        var dog = new Dog();
        dog.setId(dogId);
        dog.setCutEars(isCutEars);
        dog.setDateOfBirth(dogDateOfBirth);
        dog.setName(dogName);

        var cat = new Cat();
        cat.setId(catId);
        cat.setName(catName);
        cat.setDateOfBirth(catDateOfBirth);
        cat.setMiceCaughtNumber(miceCaughtNumber);

        var petCollection = List.of(cat, dog);

        var user = new User(id, name, email, animalPoints, petCollection);
        var userList = newArrayList(user);
        dog.setOwners(userList);
        cat.setOwners(userList);

        user.setPets(petCollection);
        when(mockUserRepository.findByName(name)).thenReturn(userList);

        final List<Integer> ownersIdList = List.of(user.getId());
        var catOutputDto = new PetOutputDto(catId, catName, catDateOfBirth, ownersIdList);
        var dogOutputDto = new PetOutputDto(dogId, dogName, dogDateOfBirth, ownersIdList);

        var petOutputDtoList = newArrayList(catOutputDto, dogOutputDto);

        var userOutputDto = new UserOutputDto(id, name, email, animalPoints, petOutputDtoList);
        var expectedUserOutputDtoCollection = newArrayList(userOutputDto);
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
        var name = validName();
        var email = validEmail();
        var animalPoints = validAnimalPoints();
        var petId1 = id();
        var petName1 = validName();
        var petDB1 = validDateOfBirth();
        var petOwnerIds1 = new ArrayList<Integer>();
        var petOutputDto1 = new PetOutputDto(petId1, petName1, petDB1, petOwnerIds1);
        var pet1 = new Cat();
        pet1.setId(petId1);
        pet1.setName(petName1);
        pet1.setDateOfBirth(petDB1);

        var petId2 = id();
        var petName2 = validName();
        var petDB2 = validDateOfBirth();
        var petOwnerIds2 = new ArrayList<Integer>();
        var petOutputDto2 = new PetOutputDto(petId2, petName2, petDB2, petOwnerIds2);
        var pet2 = new Dog();
        pet2.setId(petId2);
        pet2.setName(petName2);
        pet2.setDateOfBirth(petDB2);
        var catsIds = List.of(petId1);
        var dogsIds = List.of(petId2);

        var id = id();
        var userInputDto = new UserInputDto(name, email, animalPoints, catsIds, dogsIds);

        var petOutputDtoList = newArrayList(petOutputDto1, petOutputDto2);
        var expectedUserOutputDto = new UserOutputDto(id, name, email, animalPoints, petOutputDtoList);

        var user = new User(id, name, email, animalPoints, List.of(pet1, pet2));

        var ownersList = List.of(user);
        pet1.setOwners(ownersList);
        pet2.setOwners(ownersList);

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
        var name = invalidName();
        var email = invalidEmail();
        var animalPoints = invalidAnimalPoints();
        var catsIds = new ArrayList<Integer>();
        var dogsIds = new ArrayList<Integer>();

        var userInputDto = new UserInputDto(name, email, animalPoints, catsIds, dogsIds);

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
        var idInt = id();
        var idStr = String.valueOf(idInt);
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
        var id = id();
        var name = validName();
        var email = validEmail();
        var animalPoints = validAnimalPoints();
        var catsIds = new ArrayList<Integer>();
        var dogsIds = new ArrayList<Integer>();
        var petId1 = id();
        var petName1 = validName();
        var petDB1 = validDateOfBirth();
        var petId2 = id();
        var petName2 = validName();
        var petDB2 = validDateOfBirth();

        var userInputDto = new UserInputDto(name, email, animalPoints, catsIds, dogsIds);

        var pet1 = new Cat();
        pet1.setId(petId1);
        pet1.setName(petName1);
        pet1.setDateOfBirth(petDB1);

        var pet2 = new Dog();
        pet2.setId(petId2);
        pet2.setName(petName2);
        pet2.setDateOfBirth(petDB2);

        var user = new User(id, name, email, animalPoints, newArrayList(pet1, pet2));
        pet1.setOwners(newArrayList(user));
        pet2.setOwners(newArrayList(user));

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
        var idInt = id();
        var idStr = String.valueOf(idInt);
        var name = invalidName();
        var email = invalidEmail();
        var animalPoints = invalidAnimalPoints();
        var catsIds = new ArrayList<Integer>();
        var dogsIds = new ArrayList<Integer>();

        var userInputDto = new UserInputDto(name, email, animalPoints, catsIds, dogsIds);

        var expectedErrors = "\"Name 'No' should be between 5 and 60 symbols; " +
                "This email is not valid; " +
                "Number '-78' must be positive or zero; ";
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
        var senderId = id();
        var name1 = validName();
        var email1 = validEmail();
        var animalPoints1 = validAnimalPoints();
        var pets1 = new ArrayList<Pet>();

        var sender = new User(senderId, name1, email1, animalPoints1, pets1);

        var recipientId = id();
        var name2 = validName();
        var email2 = validEmail();
        var animalPoints2 = validAnimalPoints();
        var pets2 = new ArrayList<Pet>();

        var recipient = new User(recipientId, name2, email2, animalPoints2, pets2);

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
        var senderId = id();
        var recipientId = senderId;

        var name1 = validName();
        var email1 = validEmail();
        var animalPoints1 = validAnimalPoints();
        var pets1 = new ArrayList<Pet>();

        var user = new User(senderId, name1, email1, animalPoints1, pets1);

        var transferredAnimalPoints = 1;
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
        var senderId = id();
        var name1 = validName();
        var email1 = validEmail();
        var animalPoints1 = validAnimalPoints();
        var pets1 = new ArrayList<Pet>();

        var sender = new User(senderId, name1, email1, animalPoints1, pets1);

        var recipientId = id();
        var name2 = validName();
        var email2 = validEmail();
        var animalPoints2 = validAnimalPoints();
        var pets2 = new ArrayList<Pet>();

        var recipient = new User(recipientId, name2, email2, animalPoints2, pets2);

        var transferredAnimalPoints = 10000;
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
        var senderId = id();
        var recipientId = id();
        var transferredAnimalPoints = 30;
        when(mockUserRepository.findById(senderId)).thenThrow(NoSuchElementException.class);
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
        var senderId = id();
        var name1 = validName();
        var email1 = validEmail();
        var animalPoints1 = validAnimalPoints();
        var pets1 = new ArrayList<Pet>();

        var sender = new User(senderId, name1, email1, animalPoints1, pets1);

        var recipientId = id();
        var transferredAnimalPoints = 30;
        doNothing().when(mockUserValidator).validatePointsTransfer(senderId, recipientId, transferredAnimalPoints);
        when(mockUserRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(mockUserRepository.findById(recipientId)).thenThrow(NoSuchElementException.class);
        //Then
        assertThrows(ValidationFailedException.class, () -> userService.pointsTransfer(senderId, recipientId, transferredAnimalPoints));
        verify(mockUserRepository, times(1)).findById(recipientId);
        verify(mockUserRepository).findById(recipientId);
        verifyNoInteractions(mockUserValidator, mockUserDtoConverter);
    }
}