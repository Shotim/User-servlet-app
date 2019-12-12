package com.leverx.entity.pet.entity;

import com.leverx.entity.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;

import static com.leverx.validator.EntityValidator.MAX_SIZE;
import static com.leverx.validator.EntityValidator.MIN_SIZE;
import static com.leverx.validator.EntityValidator.NOT_VALID_DATE;
import static com.leverx.validator.EntityValidator.NOT_VALID_NAME;
import static com.leverx.validator.EntityValidator.SHOULD_NOT_BE_EMPTY;
import static java.util.Collections.emptyList;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "pets")
@Entity
@Inheritance(strategy = JOINED)
public abstract class Pet {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    int id;

    @Column
    @NonNull
    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @Size(min = MIN_SIZE, max = MAX_SIZE, message = NOT_VALID_NAME)
    String name;

    @Column
    @NonNull
    @NotNull(message = SHOULD_NOT_BE_EMPTY)
    @PastOrPresent(message = NOT_VALID_DATE)
    LocalDate dateOfBirth;

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "user_pet",
            joinColumns = {@JoinColumn(name = "petId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")})
    Collection<User> owners = emptyList();
}