package com.leverx.dog.entity;

import com.leverx.pet.entity.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "dogs")
@PrimaryKeyJoinColumn(name = "dogId")
public class Dog extends Pet {

    @Column
    boolean isCutEars;

    public Dog(String name, LocalDate dateOfBirth, boolean isCutEars) {
        super(name, dateOfBirth);
        this.isCutEars = isCutEars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog that = (Dog) o;
        return (isCutEars == that.isCutEars
                && ((Dog) o).getDateOfBirth().isEqual(that.getDateOfBirth())
                && ((Dog) o).getId() == that.getId()
                && ((Dog) o).getName().equals(that.getName())
                && ((Dog) o).getOwners().equals(that.getOwners()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCutEars, this.getId(), getName(), getDateOfBirth());
    }
}
