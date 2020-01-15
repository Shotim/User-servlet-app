package com.leverx.pet.servlet;

import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class PetServletTest {

    @InjectMocks
    private PetServlet petServlet = new PetServlet();

    @Mock
    private PetService mockPetService;

    @Mock
    private HttpServletRequest request;

    @Spy
    private HttpServletResponse response;

    @BeforeEach
    void init() {
        initMocks(this);
        response.setContentType("application/json; charset=UTF-8");
    }

    @Test
    void doGet_GivenUrlFindAllPets_ShouldWriteThemToResponseBody() throws IOException {

        //Given
        var id1 = 1;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var name1 = "vasya";
        var expectedPetOutputDto1 = new PetOutputDto(id1, name1, dateOfBirth1);
        expectedPetOutputDto1.setOwnerIds(asList(5, 1, 6));

        var id2 = 2;
        var dateOfBirth2 = LocalDate.of(2019, 12, 1);
        var name2 = "petya";
        var expectedPetOutputDto2 = new PetOutputDto(id2, name2, dateOfBirth2);
        expectedPetOutputDto2.setOwnerIds(asList(5, 1, 7));

        var id3 = 3;
        var dateOfBirth3 = LocalDate.of(2020, 1, 1);
        var name3 = "dog";
        var expectedPetOutputDto3 = new PetOutputDto(id3, name3, dateOfBirth3);
        expectedPetOutputDto3.setOwnerIds(emptyList());

        var id4 = 4;
        var dateOfBirth4 = LocalDate.of(2020, 1, 2);
        var name4 = "cat";
        var expectedPetOutputDto4 = new PetOutputDto(id4, name4, dateOfBirth4);
        expectedPetOutputDto4.setOwnerIds(emptyList());

        var expectedPetOutputDtoList = new ArrayList<>(
                List.of(expectedPetOutputDto1, expectedPetOutputDto4, expectedPetOutputDto2, expectedPetOutputDto3));

        var expectedResult = ("{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}\n" +
                "{\"id\":4,\"name\":\"cat\",\"dateOfBirth\":\"2020-01-02\",\"ownerIds\":[]}\n" +
                "{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7]}\n" +
                "{\"id\":3,\"name\":\"dog\",\"dateOfBirth\":\"2020-01-01\",\"ownerIds\":[]}")
                .replaceAll("\n", "").replaceAll("\r", "");

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/pets");

        when(mockPetService.findAll()).thenReturn(expectedPetOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        petServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockPetService, times(1)).findAll();
        verify(mockPetService).findAll();

    }

    @Test
    void doGet_GivenUrlFindByIdPet_ShouldWriteItToResponseBody() throws IOException {

        //Given
        var id1 = 1;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var name1 = "vasya";

        var expectedPetOutputDto1 = new PetOutputDto(id1, name1, dateOfBirth1);
        expectedPetOutputDto1.setOwnerIds(asList(5, 1, 6));

        var expectedResult = "{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/pets/1");

        when(mockPetService.findById(id1)).thenReturn(expectedPetOutputDto1);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        petServlet.doGet(request, response);

        //Then
        assertEquals(stringWriter.getBuffer().toString().trim(), expectedResult.trim());
        verify(mockPetService, times(1)).findById(id1);
        verify(mockPetService).findById(id1);
    }

    @Test
    void doGet_GivenUrlFindByIdPet_ShouldReturnEmptyBody() throws IOException {

        //Given
        var id = 3;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/pets/3");

        when(mockPetService.findById(id)).thenThrow(ElementNotFoundException.class);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        petServlet.doGet(request, response);

        //Then
        assertTrue(stringWriter.getBuffer().toString().isEmpty());
        verify(mockPetService, times(1)).findById(id);
        verify(mockPetService).findById(id);
    }
}