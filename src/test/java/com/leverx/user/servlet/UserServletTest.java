package com.leverx.user.servlet;

import com.leverx.cat.dto.CatOutputDto;
import com.leverx.cat.service.CatService;
import com.leverx.core.exception.ElementNotFoundException;
import com.leverx.core.exception.ValidationFailedException;
import com.leverx.dog.dto.DogOutputDto;
import com.leverx.dog.service.DogService;
import com.leverx.pet.dto.PetOutputDto;
import com.leverx.pet.service.PetService;
import com.leverx.user.dto.UserInputDto;
import com.leverx.user.dto.UserOutputDto;
import com.leverx.user.service.UserService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UserServletTest {

    @InjectMocks
    private UserServlet userServlet = new UserServlet();

    @Mock
    private UserService mockUserService;
    @Mock
    private CatService mockCatService;
    @Mock
    private DogService mockDogService;
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
    void doGet_GivenURlFindAllUsers_ShouldWriteThemToResponseBody() throws IOException {

        //Given
        var expectedResult = ("{\"id\":1,\"name\":\"Updated4\",\"email\":\"qwert@qwer\",\"animalPoints\":157,\"" +
                "pets\":[{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7]},{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}]}\n" +
                "{\"id\":5,\"name\":\"Noweqwert\",\"email\":\"qwerty@qwerty\",\"animalPoints\":207,\"" +
                "pets\":[{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7]},{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}]}")
                .replaceAll("\n", "").replaceAll("\r", "");


        var userId1 = 1;
        var userName1 = "Updated4";
        var userEmail1 = "qwert@qwer";
        final int userAnimalPoints1 = 157;

        var userId2 = 5;
        var userName2 = "Noweqwert";
        var userEmail2 = "qwerty@qwerty";
        var userAnimalPoints2 = 207;

        var petId1 = 2;
        var petName1 = "petya";
        var petDateOfBirth1 = LocalDate.of(2019, 12, 1);
        var petOwnerIds1 = List.of(5, 1, 7);
        var petOutputDto1 = new PetOutputDto(petId1, petName1, petDateOfBirth1, petOwnerIds1);

        var petId2 = 1;
        var petName2 = "vasya";
        var petDateOfBirth2 = LocalDate.of(2019, 12, 1);
        var petOwnerIds2 = List.of(5, 1, 6);
        var petOutputDto2 = new PetOutputDto(petId2, petName2, petDateOfBirth2, petOwnerIds2);

        var petOutputDtoList = new ArrayList<>(List.of(petOutputDto1, petOutputDto2));
        var userOutputDto1 = new UserOutputDto(userId1, userName1, userEmail1, userAnimalPoints1, petOutputDtoList);
        var userOutputDto2 = new UserOutputDto(userId2, userName2, userEmail2, userAnimalPoints2, petOutputDtoList);
        var expectedUserOutputDtoList = List.of(userOutputDto1, userOutputDto2);

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users");

        when(mockUserService.findAll()).thenReturn(expectedUserOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockUserService, times(1)).findAll();
        verify(mockUserService).findAll();

    }

    @Test
    void doGet_GivenURLFindById_ShouldWriteUserToResponseBody() throws IOException {

        //Given
        var expectedResult = "{\"id\":1,\"name\":\"Updated4\",\"email\":\"qwert@qwer\",\"animalPoints\":157,\"" +
                "pets\":[{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7]},{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}]}";

        var userId1 = 1;
        var userName1 = "Updated4";
        var userEmail1 = "qwert@qwer";
        var userAnimalPoints1 = 157;

        var petId1 = 2;
        var petName1 = "petya";
        var petDateOfBirth1 = LocalDate.of(2019, 12, 1);
        var petOwnerIds1 = List.of(5, 1, 7);
        var petOutputDto1 = new PetOutputDto(petId1, petName1, petDateOfBirth1, petOwnerIds1);

        var petId2 = 1;
        var petName2 = "vasya";
        var petDateOfBirth2 = LocalDate.of(2019, 12, 1);
        var petOwnerIds2 = List.of(5, 1, 6);
        var petOutputDto2 = new PetOutputDto(petId2, petName2, petDateOfBirth2, petOwnerIds2);

        var petOutputDtoList = new ArrayList<>(List.of(petOutputDto1, petOutputDto2));
        var userOutputDto = new UserOutputDto(userId1, userName1, userEmail1, userAnimalPoints1, petOutputDtoList);

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users/1");

        when(mockUserService.findById(userId1)).thenReturn(userOutputDto);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();
        assertEquals(expectedResult, actualResult);
        verify(mockUserService, times(1)).findById(userId1);
        verify(mockUserService).findById(userId1);
    }

    @Test
    void doGet_GivenURLFindById_ShouldReturnEmptyBody() throws IOException {

        //Given
        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users/1");

        when(mockUserService.findById(1)).thenThrow(ElementNotFoundException.class);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doGet(request, response);

        //Then
        assertTrue(stringWriter.getBuffer().toString().isEmpty());
        verify(mockUserService, times(1)).findById(1);
        verify(mockUserService).findById(1);

    }

    @Test
    void doGet_GivenURlFindByNameUsers_ShouldWriteThemToResponseBody() throws IOException {
        var expectedResult = ("{\"id\":1,\"name\":\"Updated\",\"email\":\"qwert@qwer\",\"animalPoints\":157,\"" +
                "pets\":[{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7]},{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}]}\n" +
                "{\"id\":5,\"name\":\"Updated\",\"email\":\"qwerty@qwerty\",\"animalPoints\":207,\"" +
                "pets\":[{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7]},{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}]}")
                .replaceAll("\n", "").replaceAll("\r", "");

        var userId1 = 1;
        var userName1 = "Updated";
        var userEmail1 = "qwert@qwer";
        final int userAnimalPoints1 = 157;

        var userId2 = 5;
        var userName2 = "Updated";
        var userEmail2 = "qwerty@qwerty";
        var userAnimalPoints2 = 207;

        var petId1 = 2;
        var petName1 = "petya";
        var petDateOfBirth1 = LocalDate.of(2019, 12, 1);
        var petOwnerIds1 = List.of(5, 1, 7);
        var petOutputDto1 = new PetOutputDto(petId1, petName1, petDateOfBirth1, petOwnerIds1);

        var petId2 = 1;
        var petName2 = "vasya";
        var petDateOfBirth2 = LocalDate.of(2019, 12, 1);
        var petOwnerIds2 = List.of(5, 1, 6);
        var petOutputDto2 = new PetOutputDto(petId2, petName2, petDateOfBirth2, petOwnerIds2);

        var petOutputDtoList = new ArrayList<>(List.of(petOutputDto1, petOutputDto2));
        var userOutputDto1 = new UserOutputDto(userId1, userName1, userEmail1, userAnimalPoints1, petOutputDtoList);
        var userOutputDto2 = new UserOutputDto(userId2, userName2, userEmail2, userAnimalPoints2, petOutputDtoList);
        var expectedUserOutputDtoList = List.of(userOutputDto1, userOutputDto2);

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users");

        when(mockUserService.findByName("Updated")).thenReturn(expectedUserOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getParameter("name")).thenReturn("Updated");
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockUserService, times(1)).findByName("Updated");
        verify(mockUserService).findByName("Updated");
    }

    @Test
    void doGet_GivenURLFindUsersCats_ShouldWriteThemToResponseBody() throws IOException {

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
        expectedCatOutputDto2.setOwnerIds(asList(5, 1, 7));
        expectedCatOutputDto2.setMiceCaughtNumber(miceCaughtNumber2);

        var expectedCatOutputDtoList = new ArrayList<>(List.of(expectedCatOutputDto1, expectedCatOutputDto2));

        var expectedResult = ("{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6],\"miceCaughtNumber\":7}\n" +
                "{\"id\":4,\"name\":\"cat\",\"dateOfBirth\":\"2020-01-02\",\"ownerIds\":[5,1,7],\"miceCaughtNumber\":0}")
                .replaceAll("\n", "").replaceAll("\r", "");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/users/1/cats");

        when(mockCatService.findByOwner(1)).thenReturn(expectedCatOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockCatService, times(1)).findByOwner(1);
        verify(mockCatService).findByOwner(1);
    }

    @Test
    void doGet_GivenURLFindUsersDogs_ShouldWriteThemToResponseBody() throws IOException {

        //Given
        var id1 = 2;
        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
        var isCutEars1 = true;
        var name1 = "petya";

        var expectedDogOutputDto1 = new DogOutputDto(id1, name1, dateOfBirth1);
        expectedDogOutputDto1.setOwnerIds(asList(5, 1, 7));
        expectedDogOutputDto1.setCutEars(isCutEars1);

        var id2 = 3;
        var dateOfBirth2 = LocalDate.of(2020, 1, 1);
        var isCutEars2 = false;
        var name2 = "dog";

        var expectedDogOutputDto2 = new DogOutputDto(id2, name2, dateOfBirth2);
        expectedDogOutputDto2.setOwnerIds(asList(5, 1, 6));
        expectedDogOutputDto2.setCutEars(isCutEars2);

        var expectedDogOutputDtoList = new ArrayList<>(List.of(expectedDogOutputDto1, expectedDogOutputDto2));

        var expectedResult = ("{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7],\"cutEars\":true}\n" +
                "{\"id\":3,\"name\":\"dog\",\"dateOfBirth\":\"2020-01-01\",\"ownerIds\":[5,1,6],\"cutEars\":false}")
                .replaceAll("\n", "").replaceAll("\r", "");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer("http://localhost:8080/users/1/dogs");

        when(mockDogService.findByOwner(1)).thenReturn(expectedDogOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockDogService, times(1)).findByOwner(1);
        verify(mockDogService).findByOwner(1);
    }

    @Test
    void doGet_GivenURLFindUsersPets_ShouldWriteThemToResponseBody() throws IOException {

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

        var expectedPetOutputDtoList = new ArrayList<>(List.of(expectedPetOutputDto1, expectedPetOutputDto2));

        var expectedResult = ("{\"id\":1,\"name\":\"vasya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,6]}\n" +
                "{\"id\":2,\"name\":\"petya\",\"dateOfBirth\":\"2019-12-01\",\"ownerIds\":[5,1,7]}")
                .replaceAll("\n", "").replaceAll("\r", "");

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users/1/pets");

        when(mockPetService.findByOwner(1)).thenReturn(expectedPetOutputDtoList);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doGet(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim()
                .replaceAll("\n", "").replaceAll("\r", "");

        assertEquals(expectedResult, actualResult);
        verify(mockPetService, times(1)).findByOwner(1);
        verify(mockPetService).findByOwner(1);
    }

    @Test
    void doPost_GivenCorrectUserData_ShouldSaveIt() throws IOException {

        //Given
        var requestBody = "{\n" +
                "\t\"name\":\"Nowqwe\",\n" +
                "\t\"email\":\"qwert@yrty\",\n" +
                "\t\"animalPoints\": 78,\n" +
                "\t\"catsIds\":[1],\n" +
                "\t\"dogsIds\":[2]\n" +
                "}";

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

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

        var petOutputDto1 = new PetOutputDto(1, "vasya", LocalDate.of(2019, 12, 1), List.of(1, 2, 3));
        var petOutputDto2 = new PetOutputDto(2, "petya", LocalDate.of(2019, 12, 1), List.of(1, 2, 3));
        var userOutputDto = new UserOutputDto(id, name, email, animalPoints, List.of(petOutputDto1, petOutputDto2));

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users");

        when(request.getReader()).thenReturn(bufferedReader);
        when(mockUserService.save(userInputDto)).thenReturn(userOutputDto);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doPost(request, response);

        //Then
        verify(mockUserService, times(1)).save(userInputDto);
        verify(mockUserService).save(userInputDto);

    }

    @Test
    void doPost_GivenIncorrectUserData_ShouldWriteErrorMessages() throws IOException {

        //Given
        var requestBody = "{\n" +
                "\t\"name\":\"Now\",\n" +
                "\t\"email\":\"qwertyrty\",\n" +
                "\t\"animalPoints\": -78,\n" +
                "\t\"catsIds\":[2],\n" +
                "\t\"dogsIds\":[1]\n" +
                "}";
        var expectedResult = "\"This email is not valid;" +
                " Number '-78' must be positive or zero;" +
                " Name 'Now' should be between 5 and 60 symbols;" +
                " 2: Cat with this id does not exist in database;" +
                " 1: Dog with this id does not exist in database\"";
        var errorMessage = "This email is not valid;" +
                " Number '-78' must be positive or zero;" +
                " Name 'Now' should be between 5 and 60 symbols;" +
                " 2: Cat with this id does not exist in database;" +
                " 1: Dog with this id does not exist in database";
        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var name = "Now";
        var email = "qwertyrty";
        var animalPoints = -78;
        var catsIds = List.of(2);
        var dogsIds = List.of(1);

        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users");

        when(request.getReader()).thenReturn(bufferedReader);
        when(mockUserService.save(userInputDto)).thenThrow(new ValidationFailedException(errorMessage));
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doPost(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();
        assertEquals(expectedResult, actualResult);

        verify(mockUserService, times(1)).save(userInputDto);
        verify(mockUserService).save(userInputDto);

    }

    @Test
    void doPost_GivenURLWithCorrectParametersForTransfer_ShouldPerformMoneyTransfer() throws IOException {

        //Given
        when(request.getParameter("recipientId")).thenReturn("5");
        when(request.getParameter("points")).thenReturn("40");

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users/11/transfer");

        doNothing().when(mockUserService).pointsTransfer(11, 5, 40);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doPost(request, response);

        //Then
        verify(mockUserService, times(1)).pointsTransfer(11, 5, 40);
        verify(mockUserService).pointsTransfer(11, 5, 40);
    }

    @Test
    void doPost_GivenURLWithIncorrectParametersForTransfer_ShouldWriteErrorMessages() throws IOException {

        //Given
        when(request.getParameter("recipientId")).thenReturn("5");
        when(request.getParameter("points")).thenReturn("40");

        var expectedResult = "\"You can't transfer points to yourself; Not enough money\"";
        var errorMessage = "You can't transfer points to yourself; Not enough money";

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users/5/transfer");

        var validationFailedException = new ValidationFailedException(errorMessage);

        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);
        doThrow(validationFailedException).when(mockUserService).pointsTransfer(5, 5, 40);

        //When
        userServlet.doPost(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();
        assertEquals(expectedResult, actualResult);

        verify(mockUserService, times(1)).pointsTransfer(5, 5, 40);
        verify(mockUserService).pointsTransfer(5, 5, 40);
    }

    @Test
    void doDelete_GivenURLForDeleteUser_ShouldDeleteIt() {

        //Given
        var stringBuffer = new StringBuffer("http://localhost:8080/users/5");
        var id = "5";

        when(request.getRequestURL()).thenReturn(stringBuffer);
        doNothing().when(mockUserService).deleteById(id);

        //When
        userServlet.doDelete(request, response);

        //Then
        verify(mockUserService, times(1)).deleteById(id);
        verify(mockUserService).deleteById(id);
    }

    @Test
    void doPut_GivenCorrectData_ShouldUpdateUserAndReturnEmptyBody() throws IOException {

        //Given
        var requestBody = "{\n" +
                "\t\"name\":\"Nowqwe\",\n" +
                "\t\"email\":\"qwert@yrty\",\n" +
                "\t\"animalPoints\": 78,\n" +
                "\t\"catsIds\":[1],\n" +
                "\t\"dogsIds\":[2]\n" +
                "}";

        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var name = "Nowqwe";
        var email = "qwert@yrty";
        var animalPoints = 78;
        var catsIds = List.of(1);
        var dogsIds = List.of(2);

        var id = "5";
        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users/5");

        when(request.getReader()).thenReturn(bufferedReader);
        doNothing().when(mockUserService).updateById(id, userInputDto);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doPut(request, response);

        //Then
        assertTrue(stringWriter.getBuffer().toString().isEmpty());
        verify(mockUserService, times(1)).updateById(id, userInputDto);
        verify(mockUserService).updateById(id, userInputDto);
    }

    @Test
    void doPut_GivenIncorrectData_ShouldWriteErrorMessagesToResponseBody() throws IOException {

        //Given
        var requestBody = "{\n" +
                "\t\"name\":\"Now\",\n" +
                "\t\"email\":\"qwertyrty\",\n" +
                "\t\"animalPoints\": -78,\n" +
                "\t\"catsIds\":[2],\n" +
                "\t\"dogsIds\":[1]\n" +
                "}";
        var expectedResult = "\"This email is not valid;" +
                " Number '-78' must be positive or zero;" +
                " Name 'Now' should be between 5 and 60 symbols;" +
                " 2: Cat with this id does not exist in database;" +
                " 1: Dog with this id does not exist in database\"";
        var errorMessage = "This email is not valid;" +
                " Number '-78' must be positive or zero;" +
                " Name 'Now' should be between 5 and 60 symbols;" +
                " 2: Cat with this id does not exist in database;" +
                " 1: Dog with this id does not exist in database";
        var stringReader = new StringReader(requestBody);
        var bufferedReader = new BufferedReader(stringReader);

        var id = "5";
        var name = "Now";
        var email = "qwertyrty";
        var animalPoints = -78;
        var catsIds = List.of(2);
        var dogsIds = List.of(1);

        var userInputDto = new UserInputDto();
        userInputDto.setName(name);
        userInputDto.setEmail(email);
        userInputDto.setAnimalPoints(animalPoints);
        userInputDto.setCatsIds(catsIds);
        userInputDto.setDogsIds(dogsIds);

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        var stringBuffer = new StringBuffer("http://localhost:8080/users/5");

        when(request.getReader()).thenReturn(bufferedReader);
        doThrow(new ValidationFailedException(errorMessage)).when(mockUserService).updateById(id, userInputDto);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(response.getWriter()).thenReturn(printWriter);

        //When
        userServlet.doPut(request, response);

        //Then
        var actualResult = stringWriter.getBuffer().toString().trim();

        assertEquals(expectedResult, actualResult);
        verify(mockUserService, times(1)).updateById(id, userInputDto);
        verify(mockUserService).updateById(id, userInputDto);
    }
}