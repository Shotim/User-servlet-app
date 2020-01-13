package com.leverx.cat.service;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.entity.Cat;
import com.leverx.cat.repository.CatRepository;
import com.leverx.cat.repository.CatRepositoryImpl;
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

import static com.leverx.core.config.BeanFactory.getCatService;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CatServiceTest {

    private CatRepository mockCatRepository = mock(CatRepositoryImpl.class);

    @InjectMocks
    private CatService catService = getCatService();

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void shouldReturnCatList() {

        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = 7;
        var name = "petya";

        var expectedCatOutputDto = new CatOutputDto(id, name, dateOfBirth);
        expectedCatOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedCatOutputDto.setMiceCaughtNumber(isCutEars);
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
        expectedCat.setMiceCaughtNumber(isCutEars);
        expectedCat.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockCatRepository.findAll()).thenReturn(List.of(expectedCat));

        //When
        var actualCatOutputDtoList = catService.findAll();

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
    }

    @Test
    void shouldReturnEmptyList() {

        //Given
        when(mockCatRepository.findAll()).thenReturn(emptyList());
        var expectedCatOutputDtoList = new ArrayList<CatOutputDto>(emptyList());

        //When
        var actualCatOutputDtoList = catService.findAll();

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
    }

    @Test
    void givenExistingId_ShouldReturnExistingCat() {

        //Given
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = 7;
        var name = "petya";

        var expectedCatOutputDto = new CatOutputDto(id, name, dateOfBirth);
        expectedCatOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedCatOutputDto.setMiceCaughtNumber(isCutEars);

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
        expectedCat.setMiceCaughtNumber(isCutEars);
        expectedCat.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockCatRepository.findById(id)).thenReturn(Optional.of(expectedCat));

        //When
        var actualCat = catService.findById(id);

        //Then
        assertEquals(expectedCatOutputDto, actualCat);
        verify(mockCatRepository).findById(id);
    }

    @Test
    void givenNonexistentId_ShouldThrownElementNotFoundException() {

        //Given
        when(mockCatRepository.findById(anyInt())).thenThrow(NoResultException.class);

        //When
        Executable whenStatement = () -> catService.findById(anyInt());

        //Then
        assertThrows(NoResultException.class, whenStatement);
    }

    @Test
    void givenExistingOwnerId_ShouldReturnExistingCats() {

        //Given
        int ownerId = 1;
        var id = 2;
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = 7;
        var name = "petya";

        var expectedCatOutputDto = new CatOutputDto(id, name, dateOfBirth);
        expectedCatOutputDto.setOwnerIds(asList(5, 1, 7));
        expectedCatOutputDto.setMiceCaughtNumber(isCutEars);
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
        expectedCat.setMiceCaughtNumber(isCutEars);
        expectedCat.setOwners(asList(userWithId5, userWithId1, userWithId7));

        when(mockCatRepository.findByOwner(ownerId)).thenReturn(List.of(expectedCat));

        //When
        var actualCatOutputDtoList = catService.findByOwner(ownerId);

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);

    }

    @Test
    void givenNonexistentOwnerId_ShouldReturnEmptyList() {

        //Given
        when(mockCatRepository.findByOwner(anyInt())).thenReturn(emptyList());
        var expectedCatOutputDtoList = new ArrayList<CatOutputDto>(emptyList());

        //When
        var actualCatOutputDtoList = catService.findByOwner(anyInt());

        //Then
        assertEquals(expectedCatOutputDtoList, actualCatOutputDtoList);
    }

    @Test
    void givenCatInputDto_ShouldSaveItAndReturnCatOutputDto() {

        //Given
        var name = "myCat";
        var dateOfBirth = LocalDate.of(2019, 12, 1);
        var isCutEars = 7;

        var cat = new Cat(name, dateOfBirth, isCutEars);

        var catInputDto = new CatInputDto();
        catInputDto.setMiceCaughtNumber(isCutEars);
        catInputDto.setDateOfBirth(dateOfBirth);
        catInputDto.setName(name);

        var expectedCatOutputDto = new CatOutputDto(cat.getId(), name, dateOfBirth);
        expectedCatOutputDto.setMiceCaughtNumber(isCutEars);

        when(mockCatRepository.save(cat)).thenReturn(Optional.of(cat));

        //When
        var actualCatOutputDto = catService.save(catInputDto);

        //Then
        assertEquals(expectedCatOutputDto, actualCatOutputDto);
    }
}