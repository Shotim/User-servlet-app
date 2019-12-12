package com.leverx.entity.cat.entity;

import com.leverx.entity.pet.entity.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

import static com.leverx.validator.EntityValidator.NON_NEGATIVE_NUMBER;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;
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
    @PositiveOrZero(message = NON_NEGATIVE_NUMBER)
    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    int miceCaughtNumber;

    public Cat(String name, LocalDate dateOfBirth, int miceCaughtNumber) {
        super(name, dateOfBirth);
        this.miceCaughtNumber = miceCaughtNumber;
    }
}