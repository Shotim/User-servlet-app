package com.leverx.dog.repository;

import com.leverx.dog.entity.Dog;
import com.leverx.pet.entity.Pet;
import com.leverx.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.leverx.core.utils.TestUtils.cutEars;
import static com.leverx.core.utils.TestUtils.id;
import static com.leverx.core.utils.TestUtils.validAnimalPoints;
import static com.leverx.core.utils.TestUtils.validDateOfBirth;
import static com.leverx.core.utils.TestUtils.validEmail;
import static com.leverx.core.utils.TestUtils.validName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class DogRepositoryTest {

    @InjectMocks
    private DogRepository dogRepository = new DogRepositoryImpl();

    @Mock
    private EntityManagerFactory mockEntityManagerFactory;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockEntityTransaction;

    @Mock
    private CriteriaBuilder mockEntityCriteriaBuilder;

    @Mock
    private CriteriaQuery<Dog> mockCriteriaQuery;

    @Mock
    private Root<Dog> mockRoot;

    @Mock
    private TypedQuery<Dog> query;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void findAll() {

        //Given
        var dogId1 = id();
        var dogName1 = validName();
        var dogDateOfBirth1 = validDateOfBirth();
        var dogIsCutEars1 = cutEars();

        var dogId2 = id();
        var dogName2 = validName();
        var dogDateOfBirth2 = validDateOfBirth();
        var dogIsCutEars2 = cutEars();

        var userId = id();
        var userName = validName();
        var userEmail = validEmail();
        var userAnimalPoints = validAnimalPoints();

        var dog1 = new Dog();

        dog1.setId(dogId1);
        dog1.setName(dogName1);
        dog1.setDateOfBirth(dogDateOfBirth1);
        dog1.setCutEars(dogIsCutEars1);

        var dog2 = new Dog();

        dog2.setId(dogId2);
        dog2.setName(dogName2);
        dog2.setDateOfBirth(dogDateOfBirth2);
        dog2.setCutEars(dogIsCutEars2);

        var dogList = newArrayList((Pet) dog1);

        final User user = new User(userId, userName, userEmail, userAnimalPoints, dogList);

        dog1.setOwners(List.of(user));

        var expectedDogs = newArrayList(dog1, dog2);


        when(mockEntityManagerFactory.createEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.getTransaction()).thenReturn(mockEntityTransaction);
        doNothing().when(mockEntityTransaction).begin();
        doNothing().when(mockEntityTransaction).commit();
        when(mockEntityManager.getCriteriaBuilder()).thenReturn(mockEntityCriteriaBuilder);
        when(mockEntityCriteriaBuilder.createQuery(Dog.class)).thenReturn(mockCriteriaQuery);
        when(mockCriteriaQuery.from(Dog.class)).thenReturn(mockRoot);
        when(mockCriteriaQuery.select(mockRoot)).thenReturn(mockCriteriaQuery);
        when(mockEntityManager.createQuery(mockCriteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedDogs);
        doNothing().when(mockEntityManager).close();

        //When
        var actualDogs = dogRepository.findAll();

        //Then
        assertEquals(expectedDogs, actualDogs);

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