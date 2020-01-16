package com.leverx.cat.entity;

import com.leverx.pet.entity.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

import static java.util.Objects.hash;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "cats")
@PrimaryKeyJoinColumn(name = "catId")
public class Cat extends Pet {

    @Column
    @PositiveOrZero
    int miceCaughtNumber;

    public Cat(String name, LocalDate dateOfBirth, int miceCaughtNumber) {
        super(name, dateOfBirth);
        this.miceCaughtNumber = miceCaughtNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat that = (Cat) o;
        return (miceCaughtNumber == that.miceCaughtNumber
                && ((Cat) o).getDateOfBirth().isEqual(that.getDateOfBirth())
                && ((Cat) o).getId() == that.getId()
                && ((Cat) o).getName().equals(that.getName())
                && ((Cat) o).getOwners().equals(that.getOwners()));
    }

    @Override
    public int hashCode() {
        return hash(miceCaughtNumber, getId(), getName(), getDateOfBirth(), getOwners());
    }
}