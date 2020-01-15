package com.leverx.dog.service;

import com.leverx.dog.dto.DogInputDto;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.entity.Dog;
import com.leverx.dog.repository.DogRepository;
import com.leverx.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class DogServiceTest {

    @Mock
    private DogRepository mockDogRepository;

    @InjectMocks
    private DogService dogService = getDogService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void findAll_ShouldReturnDogOutputDtoCollection() {

        var id1 = 2;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var isCutEars1 = true;
        var name1 = "petya";

        var expectedDogOutputDto1 = new DogOutputDto(id1, name1, dateOfBirth1);
        expectedDogOutputDto1.setOwnerIds(asList(5, 1, 7));
        expectedDogOutputDto1.setCutEars(isCutEars1);

        var id2 = 2;
        var dateOfBirth2 = LocalDate.of(2019, 12, 1);
        var isCutEars2 = true;
        var name2 = "petya";

        var expectedDogOutputDto2 = new DogOutputDto(id2, name2, dateOfBirth2);
        expectedDogOutputDto2.setOwnerIds(asList(5, 1));
        expectedDogOutputDto2.setCutEars(isCutEars1);

        var expectedDogOutputDtoList = new ArrayList<>(List.of(expectedDogOutputDto1, expectedDogOutputDto2));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedDog1 = new Dog();
        expectedDog1.setId(id1);
        expectedDog1.setName(name1);
        expectedDog1.setDateOfBirth(dateOfBirth1);
        expectedDog1.setCutEars(isCutEars1);
        expectedDog1.setOwners(asList(userWithId5, userWithId1, userWithId7));

        var expectedDog2 = new Dog();
        expectedDog2.setId(id2);
        expectedDog2.setName(name2);
        expectedDog2.setDateOfBirth(dateOfBirth2);
        expectedDog2.setCutEars(isCutEars2);
        expectedDog2.setOwners(asList(userWithId5, userWithId1));

        when(mockDogRepository.findAll()).thenReturn(List.of(expectedDog1, expectedDog2));

        //When
        var actualDogOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
        verify(mockDogRepository, times(1)).findAll();
        verify(mockDogRepository).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList() {

        //Given
        when(mockDogRepository.findAll()).thenReturn(emptyList());
        var expectedDogOutputDtoList = new ArrayList<DogOutputDto>(emptyList());

        //When
        var actualDogOutputDtoList = dogService.findAll();

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
        verify(mockDogRepository, times(1)).findAll();
        verify(mockDogRepository).findAll();
    }

    @Test
    void findById_GivenExistingId_ShouldReturnExistingDogOutputDto() {

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
        verify(mockDogRepository, times(1)).findById(id);
        verify(mockDogRepository).findById(id);
    }

    @Test
    void findById_GivenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockDogRepository.findById(anyInt())).thenThrow(NoResultException.class);

        //When
        Executable whenStatement = () -> dogService.findById(anyInt());

        //Then
        assertThrows(NoResultException.class, whenStatement);
        verify(mockDogRepository, times(1)).findById(anyInt());
        verify(mockDogRepository).findById(anyInt());
    }

    @Test
    void findByOwner_GivenExistingOwnerId_ShouldReturnExistingDogOutputDtoCollection() {

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
        verify(mockDogRepository, times(1)).findByOwner(ownerId);
        verify(mockDogRepository).findByOwner(ownerId);
    }

    @Test
    void findByOwner_GivenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockDogRepository.findByOwner(anyInt())).thenReturn(emptyList());
        var expectedDogOutputDtoList = new ArrayList<DogOutputDto>(emptyList());

        //When
        var actualDogOutputDtoList = dogService.findByOwner(anyInt());

        //Then
        assertEquals(expectedDogOutputDtoList, actualDogOutputDtoList);
        verify(mockDogRepository, times(1)).findByOwner(anyInt());
        verify(mockDogRepository).findByOwner(anyInt());
    }

    @Test
    void save_GivenCorrectDogInputDto_ShouldSaveItAndReturnDogOutputDto() {

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
        verify(mockDogRepository, times(1)).save(dog);
        verify(mockDogRepository).save(dog);
    }
}