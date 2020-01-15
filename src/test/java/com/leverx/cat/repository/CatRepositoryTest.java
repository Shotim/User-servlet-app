package com.leverx.cat.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static com.leverx.core.config.HibernateEMFConfig.getEntityManagerFactory;

class CatRepositoryTest {

    @InjectMocks
    private CatRepository catRepository = new CatRepositoryImpl();

    @BeforeAll
    static void init() {
        getEntityManagerFactory();
    }

    @Test
    void findAll_ShouldReturnCatCollection() {

//        //Given
//
//        var id1 = 1;
//        var dateOfBirth1 = LocalDate.of(2019, 12, 1);
//        var miceCaughtNumber1 = 7;
//        var name1 = "vasya";
//
//        var id2 = 4;
//        var dateOfBirth2 = LocalDate.of(2019, 1, 2);
//        var miceCaughtNumber2 = 0;
//        var name2 = "cat";
//
//        var userWithId5 = new User();
//        userWithId5.setId(5);
//        var userWithId1 = new User();
//        userWithId1.setId(1);
//        var userWithId6 = new User();
//        userWithId6.setId(6);
//
//        var expectedCat1 = Cat.builder()
//                .miceCaughtNumber(miceCaughtNumber1)
//                .id(id1)
//                .name(name1)
//                .dateOfBirth(dateOfBirth1)
//                .owners(asList(userWithId5, userWithId1, userWithId6))
//                .build();
//
//        var expectedCat2 = Cat.builder()
//                .miceCaughtNumber(miceCaughtNumber2)
//                .id(id2)
//                .name(name2)
//                .dateOfBirth(dateOfBirth2)
//                .owners(new ArrayList<>(emptyList()))
//                .build();
//

    }

    @Test
    void findById() {
    }

    @Test
    void findByOwner() {
    }

    @Test
    void save() {
    }
}