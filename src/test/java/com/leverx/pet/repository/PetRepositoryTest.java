package com.leverx.pet.repository;

import com.leverx.cat.entity.Cat;
import com.leverx.dog.entity.Dog;
import com.leverx.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.leverx.core.config.TomcatCPConfig.createConnectionPool;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PetRepositoryTest {

    private PetRepository petRepository = new PetRepositoryImpl();

    @BeforeAll
    static void init() {
        createConnectionPool();
    }

    @Test
    void findAll_GivenExpectedData_ShouldReturnTheSame() {
        var userWithId1 = new User();
        userWithId1.setId(1);
        userWithId1.setName("User1");
        userWithId1.setEmail("qwerty@rty");
        userWithId1.setAnimalPoints(1000);

        var userWithId2 = new User();
        userWithId2.setId(2);
        userWithId2.setName("User1");
        userWithId2.setEmail("qwerty@rty");
        userWithId2.setAnimalPoints(1000);

        var id1 = 1;
        var dateOfBirth1 = LocalDate.of(2019, 9, 3);
        var miceCaughtNumber1 = 0;
        var name1 = "Caat1";
        var cat1 = new Cat();
        cat1.setMiceCaughtNumber(miceCaughtNumber1);
        cat1.setId(id1);
        cat1.setName(name1);
        cat1.setDateOfBirth(dateOfBirth1);
        cat1.setOwners(asList(userWithId1, userWithId2));

        var id2 = 2;
        var dateOfBirth2 = LocalDate.of(2019, 9, 3);
        var miceCaughtNumber2 = 0;
        var name2 = "Caat2";
        var cat2 = new Cat();
        cat2.setMiceCaughtNumber(miceCaughtNumber2);
        cat2.setId(id2);
        cat2.setName(name2);
        cat2.setDateOfBirth(dateOfBirth2);
        cat2.setOwners(asList(userWithId2));

        var id3 = 3;
        var dateOfBirth3 = LocalDate.of(2019, 9, 3);
        var isCutEars3 = false;
        var name3 = "Doog1";
        var dog3 = new Dog();
        dog3.setCutEars(isCutEars3);
        dog3.setId(id3);
        dog3.setName(name3);
        dog3.setDateOfBirth(dateOfBirth3);
        dog3.setOwners(asList(userWithId1));

        var id4 = 4;
        var dateOfBirth4 = LocalDate.of(2019, 9, 3);
        var isCutEars4 = false;
        var name4 = "Doog2";
        var dog4 = new Dog();
        dog4.setCutEars(isCutEars4);
        dog4.setId(id4);
        dog4.setName(name4);
        dog4.setDateOfBirth(dateOfBirth4);
        dog4.setOwners(asList(userWithId2));

        userWithId1.setPets(asList(cat1, dog3));
        userWithId2.setPets(asList(cat1, cat2, dog4));

        var expectedPets = newArrayList(cat1, cat2, dog3, dog4);

        //When
        var actualPets = petRepository.findAll();

        //Then
        assertEquals(expectedPets, actualPets);
    }

    @Test
    void findById_GivenCorrectId_ShouldReturnPet() {

        //Given
        var userWithId1 = new User();
        userWithId1.setId(1);
        userWithId1.setName("User1");
        userWithId1.setEmail("qwerty@rty");
        userWithId1.setAnimalPoints(1000);

        var userWithId2 = new User();
        userWithId2.setId(2);
        userWithId2.setName("User1");
        userWithId2.setEmail("qwerty@rty");
        userWithId2.setAnimalPoints(1000);

        var id1 = 1;
        var dateOfBirth1 = LocalDate.of(2019, 9, 1);
        var miceCaughtNumber1 = 0;
        var name1 = "Caat1";
        var cat1 = new Cat();
        cat1.setMiceCaughtNumber(miceCaughtNumber1);
        cat1.setId(id1);
        cat1.setName(name1);
        cat1.setDateOfBirth(dateOfBirth1);
        cat1.setOwners(asList(userWithId1, userWithId2));

        var id2 = 2;
        var dateOfBirth2 = LocalDate.of(2019, 9, 3);
        var miceCaughtNumber2 = 13;
        var name2 = "Caat2";
        var cat2 = new Cat();
        cat2.setMiceCaughtNumber(miceCaughtNumber2);
        cat2.setId(id2);
        cat2.setName(name2);
        cat2.setDateOfBirth(dateOfBirth2);
        cat2.setOwners(asList(userWithId2));

        var id3 = 3;
        var dateOfBirth3 = LocalDate.of(2019, 9, 3);
        var isCutEars3 = false;
        var name3 = "Doog1";
        var dog3 = new Dog();
        dog3.setCutEars(isCutEars3);
        dog3.setId(id3);
        dog3.setName(name3);
        dog3.setDateOfBirth(dateOfBirth3);
        dog3.setOwners(asList(userWithId1));

        var id4 = 4;
        var dateOfBirth4 = LocalDate.of(2019, 9, 3);
        var isCutEars4 = false;
        var name4 = "Doog1";
        var dog4 = new Dog();
        dog4.setCutEars(isCutEars4);
        dog4.setId(id4);
        dog4.setName(name4);
        dog4.setDateOfBirth(dateOfBirth4);
        dog4.setOwners(asList(userWithId2));

        userWithId1.setPets(asList(cat1, dog3));
        userWithId2.setPets(asList(cat1, cat2, dog4));

        //When
        var actualCat = petRepository.findById(1);

        //Then
        assertEquals(cat1, actualCat.get());
    }

    @Test
    void findByOwner_GivenCorrectOwnerId_ShouldReturnListOfPets() {

        //Given
        var userWithId1 = new User();
        userWithId1.setId(1);
        userWithId1.setName("User1");
        userWithId1.setEmail("qwerty@rty");
        userWithId1.setAnimalPoints(1000);

        var userWithId2 = new User();
        userWithId2.setId(2);
        userWithId2.setName("User1");
        userWithId2.setEmail("qwerty@rty");
        userWithId2.setAnimalPoints(1000);

        var id1 = 1;
        var dateOfBirth1 = LocalDate.of(2019, 9, 1);
        var miceCaughtNumber1 = 0;
        var name1 = "Caat1";
        var cat1 = new Cat();
        cat1.setMiceCaughtNumber(miceCaughtNumber1);
        cat1.setId(id1);
        cat1.setName(name1);
        cat1.setDateOfBirth(dateOfBirth1);
        cat1.setOwners(asList(userWithId1, userWithId2));

        var id2 = 2;
        var dateOfBirth2 = LocalDate.of(2019, 9, 3);
        var miceCaughtNumber2 = 13;
        var name2 = "Caat2";
        var cat2 = new Cat();
        cat2.setMiceCaughtNumber(miceCaughtNumber2);
        cat2.setId(id2);
        cat2.setName(name2);
        cat2.setDateOfBirth(dateOfBirth2);
        cat2.setOwners(List.of(userWithId2));

        var id3 = 3;
        var dateOfBirth3 = LocalDate.of(2019, 9, 3);
        var isCutEars3 = false;
        var name3 = "Doog1";
        var dog3 = new Dog();
        dog3.setCutEars(isCutEars3);
        dog3.setId(id3);
        dog3.setName(name3);
        dog3.setDateOfBirth(dateOfBirth3);
        dog3.setOwners(List.of(userWithId1));

        var id4 = 4;
        var dateOfBirth4 = LocalDate.of(2019, 9, 3);
        var isCutEars4 = false;
        var name4 = "Doog1";
        var dog4 = new Dog();
        dog4.setCutEars(isCutEars4);
        dog4.setId(id4);
        dog4.setName(name4);
        dog4.setDateOfBirth(dateOfBirth4);
        dog4.setOwners(List.of(userWithId2));

        userWithId1.setPets(asList(cat1, dog3));
        userWithId2.setPets(asList(cat1, cat2, dog4));

        //When
        var actualPet = petRepository.findByOwner(1);

        //Then
        assertEquals(List.of(cat1, dog3), actualPet);
    }
}