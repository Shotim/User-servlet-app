package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
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

import static com.leverx.core.config.BeanFactory.getCatService;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CatServiceTest {

    @Mock
    private CatRepository mockCatRepository;

    @InjectMocks
    private CatService catService = getCatService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void findAll_ShouldReturnCatList() {

        var id1 = 2;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber1 = 7;
        var name1 = "petya";

        var expectedCatOutputDto1 = new CatOutputDto(id1, name1, dateOfBirth1);
        expectedCatOutputDto1.setOwnerIds(asList(5, 1, 7));
        expectedCatOutputDto1.setMiceCaughtNumber(miceCaughtNumber1);

        var id2 = 3;
        var dateOfBirth2 = LocalDate.of(2019, 2, 1);
        var miceCaughtNumber2 = 3;
        var name2 = "vasya";

        var expectedCatOutputDto2 = new CatOutputDto(id2, name2, dateOfBirth2);
        expectedCatOutputDto2.setOwnerIds(asList(5, 1));
        expectedCatOutputDto2.setMiceCaughtNumber(miceCaughtNumber2);

        var expectedCatOutputDtoList = new ArrayList<>(List.of(expectedCatOutputDto1, expectedCatOutputDto2));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedCat1 = new Cat();
        expectedCat1.setId(id1);
        expectedCat1.setName(name1);
        expectedCat1.setDateOfBirth(dateOfBirth1);
        expectedCat1.setMiceCaughtNumber(miceCaughtNumber1);
        expectedCat1.setOwners(asList(userWithId5, userWithId1, userWithId7));

        var expectedCat2 = new Cat();
        expectedCat2.setId(id2);
        expectedCat2.setName(name2);
        expectedCat2.setDateOfBirth(dateOfBirth2);
        expectedCat2.setMiceCaughtNumber(miceCaughtNumber2);
        expectedCat2.setOwners(asList(userWithId5, userWithId1));
        when(mockCatRepository.findAll()).thenReturn(List.of(expectedCat1, expectedCat2));

        //When
        var actualCatOutputDtoList = catService.findAll();

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
        verify(mockCatRepository, times(1)).findAll();
        verify(mockCatRepository).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList() {

        //Given
        when(mockCatRepository.findAll()).thenReturn(emptyList());
        var expectedCatOutputDtoList = new ArrayList<CatOutputDto>(emptyList());

        //When
        var actualCatOutputDtoList = catService.findAll();

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
        verify(mockCatRepository, times(1)).findAll();
        verify(mockCatRepository).findAll();
    }

    @Test
    void findById_GivenExistingId_ShouldReturnExistingCat() {

        //Given
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber = 7;
        var name = "petya";

        var expectedCatOutputDto = new CatOutputDto(id, name, dateOfBirth);
        expectedCatOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedCatOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedCat = new Cat();
        expectedCat.setId(id);
        expectedCat.setName(name);
        expectedCat.setDateOfBirth(dateOfBirth);
        expectedCat.setMiceCaughtNumber(miceCaughtNumber);
        expectedCat.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockCatRepository.findById(id)).thenReturn(Optional.of(expectedCat));

        //When
        var actualCat = catService.findById(id);

        //Then
        assertEquals(expectedCatOutputDto, actualCat);
        verify(mockCatRepository, times(1)).findById(id);
        verify(mockCatRepository).findById(id);
    }

    @Test
    void findById_GivenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockCatRepository.findById(anyInt())).thenThrow(NoResultException.class);

        //When
        Executable whenStatement = () -> catService.findById(anyInt());

        //Then
        assertThrows(NoResultException.class, whenStatement);
        verify(mockCatRepository, times(1)).findById(anyInt());
        verify(mockCatRepository).findById(anyInt());
    }

    @Test
    void findByOwner_GivenExistingOwnerId_ShouldReturnExistingCats() {

        //Given
        int ownerId = 1;
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber = 7;
        var name = "petya";

        var expectedCatOutputDto = new CatOutputDto(id, name, dateOfBirth);
        expectedCatOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedCatOutputDto.setMiceCaughtNumber(miceCaughtNumber);
        var expectedCatOutputDtoList = new ArrayList<>(List.of(expectedCatOutputDto));

        var userWithId5 = new User();
        userWithId5.setId(5);
        var userWithId1 = new User();
        userWithId1.setId(1);
        var userWithId7 = new User();
        userWithId7.setId(7);

        var expectedCat = new Cat();
        expectedCat.setId(id);
        expectedCat.setName(name);
        expectedCat.setDateOfBirth(dateOfBirth);
        expectedCat.setMiceCaughtNumber(miceCaughtNumber);
        expectedCat.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockCatRepository.findByOwner(ownerId)).thenReturn(List.of(expectedCat));

        //When
        var actualCatOutputDtoList = catService.findByOwner(ownerId);

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
        verify(mockCatRepository, times(1)).findByOwner(ownerId);
        verify(mockCatRepository).findByOwner(ownerId);

    }

    @Test
    void findByOwner_GivenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockCatRepository.findByOwner(anyInt())).thenReturn(emptyList());
        var expectedCatOutputDtoList = new ArrayList<CatOutputDto>(emptyList());

        //When
        var actualCatOutputDtoList = catService.findByOwner(anyInt());

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
        verify(mockCatRepository, times(1)).findByOwner(anyInt());
        verify(mockCatRepository).findByOwner(anyInt());
    }

    @Test
    void save_GivenCatInputDto_ShouldSaveItAndReturnCatOutputDto() {

        //Given
        var name = "myCat";
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber = 7;

        var cat = new Cat(name, dateOfBirth, miceCaughtNumber);

        var catInputDto = new CatInputDto();
        catInputDto.setMiceCaughtNumber(miceCaughtNumber);
        catInputDto.setDateOfBirth(dateOfBirth);
        catInputDto.setName(name);

        var expectedCatOutputDto = new CatOutputDto(cat.getId(), name, dateOfBirth);
        expectedCatOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        when(mockCatRepository.save(cat)).thenReturn(Optional.of(cat));

        //When
        var actualCatOutputDto = catService.save(catInputDto);

        //Then
        assertEquals(expectedCatOutputDto, actualCatOutputDto);
        verify(mockCatRepository, times(1)).save(cat);
        verify(mockCatRepository).save(cat);
    }
}