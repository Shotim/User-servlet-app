package com.leverx.pet.repository;

import com.leverx.cat.entity.Cat;
import com.leverx.core.exception.InternalServerErrorException;
import com.leverx.dog.entity.Dog;
import com.leverx.pet.entity.Pet;
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

    private Optional<Pet> getPetFromResultSet(ResultSet resultSet) throws SQLException {
        var pets = getPetsCollectionFromResultSet(resultSet);
        return pets.stream().findFirst();
    }

    private Collection<Pet> getPetsCollectionFromResultSet(ResultSet resultSet) throws SQLException {
        var pets = new ArrayList<Pet>();
        while (resultSet.next()) {
            var id = resultSet.getInt("id");
            var dateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
            var name = resultSet.getString("name");
            var miceCaughtNumber = resultSet.getInt("miceCaughtNumber");
            var isCutEars = resultSet.getBoolean("isCutEars");
            if (isADog(miceCaughtNumber, isCutEars)) {
                addPetToCollection(pets, id, dateOfBirth, name, Dog.class);
            } else {
                addPetToCollection(pets, id, dateOfBirth, name, Cat.class);
            }
        }
        return pets;
    }

    private boolean isADog(int miceCaughtNumber, boolean isCutEars) {
        return isNull(miceCaughtNumber) && !isNull(isCutEars);
    }

    private <T extends Pet> void addPetToCollection(ArrayList<Pet> pets, int id, LocalDate dateOfBirth, String name, Class<T> tClass) {
        Pet pet = null;
        try {
            pet = tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
        pet.setId(id);
        pet.setName(name);
        pet.setDateOfBirth(dateOfBirth);
        pets.add(pet);
    }

    private boolean isNull(int columnValue) {
        return columnValue == 0;
    }

    private boolean isNull(boolean columnValue) {
        return columnValue;
    }
}