package com.leverx.entity.pet.repository.utils;

import com.leverx.entity.pet.entity.Pet;
import com.leverx.entity.pet.entity.Pet_;
import com.leverx.entity.user.entity.User_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;

public class PetRepositoryUtils {


    private static <T extends Pet> CriteriaQuery<T> getPetCriteriaQueryEqualToIdParameter(int id, EntityManager entityManager, SingularAttribute<Pet, ?> attribute, Class<T> t) {

        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(t);
        var root = criteriaQuery.from(t);

        criteriaQuery.select(root);

        var path = root.get(attribute);
        var equalCondition = builder.equal(path, id);

        criteriaQuery.where(equalCondition);

        return criteriaQuery;
    }

    private static <T extends Pet> List<T> getResultList(EntityManager entityManager, CriteriaQuery<T> criteriaQuery) {
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private static <T extends Pet> CriteriaQuery<T> getPetCriteriaQuery(EntityManager entityManager, Class<T> t) {
        var builder = entityManager.getCriteriaBuilder();
        return builder.createQuery(t);
    }

    public static <T extends Pet> List<T> getAllPets(EntityManager entityManager, Class<T> petClass) {
        var criteriaQuery = getPetCriteriaQuery(entityManager, petClass);
        var root = criteriaQuery.from(petClass);

        criteriaQuery.select(root);

        return getResultList(entityManager, criteriaQuery);
    }


    public static <T extends Pet> T getPetById(int id, EntityManager entityManager, Class<T> t) {
        var criteriaQuery = getPetCriteriaQueryEqualToIdParameter(id, entityManager, Pet_.id, t);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }


    public static <T extends Pet, T_ extends Pet_> Collection<T> retrievePetsByOwner(int ownerId, EntityManager entityManager, Class<T> entityClass) {
        var builder = entityManager.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(entityClass);

        var root = criteriaQuery.from(entityClass);
        var users = root.join(T_.owners);
        var idEqualToOwnerId = builder.equal(users.get(User_.id), ownerId);
        criteriaQuery.select(root)
                .where(idEqualToOwnerId);
        var query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
