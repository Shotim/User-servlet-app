package com.leverx.dog.entity;

import com.leverx.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "dogs")
public class Dog extends Pet {

    @Column
    boolean isCutEars;

    public Dog(String name, LocalDate dateOfBirth, boolean isCutEars) {
        super(name, dateOfBirth);
        this.isCutEars = isCutEars;
    }
}
