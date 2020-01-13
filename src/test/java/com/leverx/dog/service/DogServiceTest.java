package com.leverx.dog.service;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.entity.Dog;
import com.leverx.dog.repository.DogRepository;
import com.leverx.dog.repository.DogRepositoryImpl;
import com.leverx.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.leverx.core.config.BeanFactory.getDogService;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class DogServiceTest {

    private DogRepository mockDogRepository = mock(DogRepositoryImpl.class);

    @InjectMocks
    private DogService dogService = getDogService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void shouldReturnDogList() {

        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var name = "petya";

        var expectedDogOutputDto = new DogOutputDto(id, name, dateOfBirth);
        expectedDogOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedDogOutputDto.setCutEars(isCutEars);
        var expectedDogOutputDtoList = new ArrayList<>(List.of(expectedDogOutputDto));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedDog = new Dog();
        expectedDog.setId(id);
        expectedDog.setName(name);
        expectedDog.setDateOfBirth(dateOfBirth);
        expectedDog.setCutEars(isCutEars);
        expectedDog.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockDogRepository.findAll()).thenReturn(List.of(expectedDog));

        //When
        var actualDogOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
    }

    @Test
    void shouldReturnEmptyList() {

        //Given
        when(mockDogRepository.findAll()).thenReturn(emptyList());
        var expectedDogOutputDtoList = new ArrayList<DogOutputDto>(emptyList());

        //When
        var actualDogOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
    }

    @Test
    void givenExistingId_ShouldReturnExistingDog() {

        //Given
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var name = "petya";

        var expectedDogOutputDto = new DogOutputDto(id, name, dateOfBirth);
        expectedDogOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedDogOutputDto.setCutEars(isCutEars);

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedDog = new Dog();
        expectedDog.setId(id);
        expectedDog.setName(name);
        expectedDog.setDateOfBirth(dateOfBirth);
        expectedDog.setCutEars(isCutEars);
        expectedDog.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockDogRepository.findById(id)).thenReturn(Optional.of(expectedDog));

        //When
        var actualDog = dogService.findById(id);

        //Then
        assertEquals(expectedDogOutputDto, actualDog);
        verify(mockDogRepository).findById(id);
    }

    @Test
    void givenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockDogRepository.findById(anyInt())).thenThrow(NoResultException.class);

        //When
        Executable whenStatement = () -> dogService.findById(anyInt());

        //Then
        assertThrows(NoResultException.class, whenStatement);
    }

    @Test
    void givenExistingOwnerId_ShouldReturnExistingDogs() {

        //Given
        int ownerId = 1;
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;
        var name = "petya";

        var expectedDogOutputDto = new DogOutputDto(id, name, dateOfBirth);
        expectedDogOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedDogOutputDto.setCutEars(isCutEars);
        var expectedDogOutputDtoList = new ArrayList<>(List.of(expectedDogOutputDto));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedDog = new Dog();
        expectedDog.setId(id);
        expectedDog.setName(name);
        expectedDog.setDateOfBirth(dateOfBirth);
        expectedDog.setCutEars(isCutEars);
        expectedDog.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockDogRepository.findByOwner(ownerId)).thenReturn(List.of(expectedDog));

        //When
        var actualDogOutputDtoList = dogService.findByOwner(ownerId);

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);

    }

    @Test
    void givenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockDogRepository.findByOwner(anyInt())).thenReturn(emptyList());
        var expectedDogOutputDtoList = new ArrayList<DogOutputDto>(emptyList());

        //When
        var actualDogOutputDtoList = dogService.findByOwner(anyInt());

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
    }

    @Test
    void givenDogInputDto_ShouldSaveItAndReturnDogOutputDto() {

        //Given
        var name = "myDog";
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = true;

        var dog = new Dog(name, dateOfBirth, isCutEars);

        var dogInputDto = new DogInputDto();
        dogInputDto.setCutEars(isCutEars);
        dogInputDto.setDateOfBirth(dateOfBirth);
        dogInputDto.setName(name);

        var expectedDogOutputDto = new DogOutputDto(dog.getId(), name, dateOfBirth);
        expectedDogOutputDto.setCutEars(isCutEars);

        when(mockDogRepository.save(dog)).thenReturn(Optional.of(dog));

        //When
        var actualDogOutputDto = dogService.save(dogInputDto);

        //Then
        assertEquals(expectedDogOutputDto, actualDogOutputDto);
    }
}