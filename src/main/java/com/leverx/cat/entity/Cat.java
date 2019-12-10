package com.leverx.cat.entity;

import com.leverx.pet.entity.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "cats")
public class Cat extends Pet {

    @Column
    @PositiveOrZero
    int miceCachedNumber;

    public Cat(String name, LocalDate dateOfBirth, int miceCachedNumber) {
        super(name, dateOfBirth);
        this.miceCachedNumber = miceCachedNumber;
    }
}