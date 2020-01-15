package com.leverx.cat.servlet;

import com.leverx.cat.dto.CatInputDto;
import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.service.CatService;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
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

class CatServletTest {

    @InjectMocks
    private CatServlet catServlet = new CatServlet();

    @Mock
    private CatService mockCatService;

    @Mock
    private HttpServletRequest request;

    @Spy
    private HttpServletResponse response;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void doGet_GivenUrlFindAllCats_ShouldWriteThemToResponseBody() throws IOException {

        //Given
        var id1 = 1;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber1 = 7;
        var name1 = "vasya";

        var expectedCatOutputDto1 = new CatOutputDto(id1, name1, dateOfBirth1);
        expectedCatOutputDto1.setOwnerIds(asList(5, 1, 6));
        expectedCatOutputDto1.setMiceCaughtNumber(miceCaughtNumber1);

        var id2 = 4;
        var dateOfBirth2 = LocalDate.of(2020, 1, 2);
        var miceCaughtNumber2 = 0;
        var name2 = "cat";

        var expectedCatOutputDto2 = new CatOutputDto(id2, name2, dateOfBirth2);
        expectedCatOutputDto2.setOwnerIds(emptyList());
        expectedCatOutputDto2.setMiceCaughtNumber(miceCaughtNumber2);
        var expectedCatOutputDtoList = new ArrayList<>(List.of(expectedCatOutputDto1, expectedCatOutputDto2));

        var expectedResult = ("{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6],\"miceCaughtNumber\":7}\n" +
                "{\"id\":4,\"name\":\"cat\",\"dateOfBirth\":\"2020-01-02\",\"ownerIds\":[],\"miceCaughtNumber\":0}")
                .replaceAll("\n", "").replaceAll("\r", "");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats");

        when(mockCatService.findAll()).thenReturn(expectedCatOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockCatService, times(1)).findAll();
        verify(mockCatService).findAll();
    }

    @Test
    void doGet_GivenUrlFindByIdCat_ShouldWriteItToResponseBody() throws IOException {

        //Given
        var id1 = 1;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var miceCaughtNumber1 = 7;
        var name1 = "vasya";

        var expectedCatOutputDto1 = new CatOutputDto(id1, name1, dateOfBirth1);
        expectedCatOutputDto1.setOwnerIds(asList(5, 1, 6));
        expectedCatOutputDto1.setMiceCaughtNumber(miceCaughtNumber1);

        var expectedResult = "{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6],\"miceCaughtNumber\":7}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats/1");

        when(mockCatService.findById(id1)).thenReturn(expectedCatOutputDto1);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();

        assertEquals(expectedResult, actualResult);
        verify(mockCatService, times(1)).findById(id1);
        verify(mockCatService).findById(id1);
    }

    @Test
    void doGet_GivenUrlFindByIdCat_ShouldReturnEmptyBody() throws IOException {

        //Given
        var id = 3;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats/3");

        when(mockCatService.findById(id)).thenThrow(ElementNotFoundException.class);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doGet(request, response);

        //Then
        assertTrue(stringWriter.getBuffer().toString().isEmpty());
        verify(mockCatService, times(1)).findById(id);
        verify(mockCatService).findById(id);
    }

    @Test
    void doPost_GivenSaveUrlWithCorrectCatBody_ShouldSaveIt() throws IOException {

        //Given
        var requestBody = "{\n" +
                "\t\"name\":\"Caaaaaaaaat\",\n" +
                "\t\"dateOfBirth\":\"2019-09-03\",\n" +
                "\t\"miceCaughtNumber\": \"13\"\n" +
                "}";

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var id = 3;
        var name = "Caaaaaaaaat";
        var dateOfBirth = LocalDate.of(2019, 9, 3);
        var miceCaughtNumber = 13;

        var catInputDto = new CatInputDto();
        catInputDto.setName(name);
        catInputDto.setDateOfBirth(dateOfBirth);
        catInputDto.setMiceCaughtNumber(miceCaughtNumber);

        var catOutputDto = new CatOutputDto(id, name, dateOfBirth);
        catOutputDto.setMiceCaughtNumber(miceCaughtNumber);

        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats");

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(mockCatService.save(catInputDto)).thenReturn(catOutputDto);

        //When
        catServlet.doPost(request, response);

        //Then
        verify(mockCatService).save(catInputDto);
        verify(mockCatService, times(1)).save(catInputDto);
    }

    @Test
    void doPost_GivenSaveUrlCatBody_ShouldThrownValidationFailedException() throws IOException {

        //Given
        var requestBody = "{\n" +
                "\t\"name\":\"Cat\",\n" +
                "\t\"dateOfBirth\":\"2021-09-03\",\n" +
                "\t\"miceCaughtNumber\": \"-13\"\n" +
                "}";

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var name = "Cat";
        var dateOfBirth = LocalDate.of(2021, 9, 3);
        var miceCaughtNumber = -13;

        var catInputDto = new CatInputDto();
        catInputDto.setName(name);
        catInputDto.setDateOfBirth(dateOfBirth);
        catInputDto.setMiceCaughtNumber(miceCaughtNumber);

        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/cats");

        var errorMessage = "Must be a date in the past or in the present;" +
                " Number '-13' must be positive or zero; Name 'Cat' should be between 5 and 60 symbols";

        var expectedResult = "\"Must be a date in the past or in the present;" +
                " Number '-13' must be positive or zero; Name 'Cat' should be between 5 and 60 symbols\"";

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(mockCatService.save(catInputDto)).thenThrow(new ValidationFailedException(errorMessage));
        when(response.getWriter()).thenReturn(printWriter);

        //When
        catServlet.doPost(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();

        assertEquals(expectedResult, actualResult);
        verify(mockCatService).save(catInputDto);
        verify(mockCatService, times(1)).save(catInputDto);
    }
}