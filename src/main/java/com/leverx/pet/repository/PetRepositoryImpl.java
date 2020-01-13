package com.leverx.pet.repository;

import com.leverx.cat.entity.Cat;
import com.leverx.core.exception.InternalServerErrorException;
import com.leverx.dog.entity.Dog;
import com.leverx.pet.entity.Pet;
import com.leverx.user.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.leverx.core.config.TomcatCPConfig.getConnection;

@Slf4j
public class PetRepositoryImpl implements PetRepository {

    @Override
    public Collection<Pet> findAll() {
        try (var connection = getConnection();
             var preparedStatement = connection.prepareStatement("select * from servlet_app.pets left join dogs d on pets.id = d.dogId left join cats c on pets.id = c.catId")) {
            try (var resultSet = preparedStatement.executeQuery()) {
                return getPetsCollectionFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error(e.getSQLState());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public Optional<Pet> findById(int id) {
        try (var connection = getConnection();
             var preparedStatement = connection.prepareStatement("select * from servlet_app.pets left join dogs d on pets.id = d.dogId left join cats c on pets.id = c.catId where id=?")) {
            preparedStatement.setInt(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                return getPetFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error(e.getSQLState());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public Collection<Pet> findByOwner(int ownerId) {
        try (var connection = getConnection();
             var preparedStatement = connection.prepareStatement("select * from servlet_app.pets left join dogs d on pets.id = d.dogId left join cats c on pets.id = c.catId join user_pet up on pets.id = up.petId where userId=?")) {
            preparedStatement.setInt(1, ownerId);
            try (var resultSet = preparedStatement.executeQuery()) {
                return getPetsCollectionFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error(e.getSQLState());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private Optional<Pet> getPetFromResultSet(ResultSet resultSet) throws SQLException {
        var pets = getPetsCollectionFromResultSet(resultSet);
        return pets.stream().findFirst();
    }

    private Collection<Pet> getPetsCollectionFromResultSet(ResultSet resultSet) throws SQLException {
        var pets = new ArrayList<Pet>();
        while (resultSet.next()) {
            var petId = resultSet.getInt("id");
            var petDateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
            var petName = resultSet.getString("name");
            var miceCaughtNumber = resultSet.getInt("miceCaughtNumber");
            var isCutEars = resultSet.getBoolean("isCutEars");
            var userCollection = getUserCollectionForPet(petId);
            if (isADog(miceCaughtNumber, isCutEars)) {
                addPetToCollection(pets, petId, petDateOfBirth, petName, userCollection, Dog.class);
            } else {
                addPetToCollection(pets, petId, petDateOfBirth, petName, userCollection, Cat.class);
            }
        }
        return pets;
    }

    private ArrayList<User> getUserCollectionForPet(int petId) {
        var userCollection = new ArrayList<User>();
        try (var connection = getConnection();
             var preparedStatement = connection.prepareStatement("select * from servlet_app.users join user_pet up on users.id = up.userId where petId=?")) {
            preparedStatement.setInt(1, petId);
            try (var userResultSet = preparedStatement.executeQuery()) {
                while (userResultSet.next()) {
                    var userId = userResultSet.getInt("id");
                    var userName = userResultSet.getString("name");
                    var userEmail = userResultSet.getString("email");
                    var animalPoints = userResultSet.getInt("animalPoints");
                    var user = new User();
                    user.setId(userId);
                    user.setName(userName);
                    user.setEmail(userEmail);
                    user.setAnimalPoints(animalPoints);
                    userCollection.add(user);
                }
            }
        } catch (SQLException e) {
            log.error(e.getSQLState());
            throw new InternalServerErrorException(e.getMessage());
        }
        return userCollection;
    }

    private boolean isADog(int miceCaughtNumber, boolean isCutEars) {
        return isNull(miceCaughtNumber) && !isNull(isCutEars);
    }

    private <T extends Pet> void addPetToCollection(ArrayList<Pet> pets, int id, LocalDate dateOfBirth, String name, Collection<User> owners, Class<T> tClass) {
        Pet pet = null;
        try {
            pet = tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
        pet.setId(id);
        pet.setName(name);
        pet.setDateOfBirth(dateOfBirth);
        pet.setOwners(owners);
        pets.add(pet);
    }

    private boolean isNull(int columnValue) {
        return columnValue == 0;
    }

    private boolean isNull(boolean columnValue) {
        return columnValue;
    }
}