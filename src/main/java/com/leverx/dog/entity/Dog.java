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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
    @NotNull
    boolean isCutEars;

    public Dog(String name, LocalDate dateOfBirth, boolean isCutEars) {
        super(name, dateOfBirth);
        this.isCutEars = isCutEars;
    }
}
