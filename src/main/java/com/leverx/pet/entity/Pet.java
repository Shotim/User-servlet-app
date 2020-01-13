package com.leverx.pet.entity;

import com.leverx.user.entity.User;
import lombok.EqualsAndHashCode;
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

import static com.leverx.core.validator.EntityValidator.MAX_SIZE;
import static com.leverx.core.validator.EntityValidator.MIN_SIZE;
import static java.util.Collections.emptyList;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
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
    @NotNull
    @Size(min = MIN_SIZE, max = MAX_SIZE)
    String name;

    @Column
    @NonNull
    @NotNull
    @PastOrPresent
    LocalDate dateOfBirth;

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "user_pet",
            joinColumns = {@JoinColumn(name = "petId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")})
    Collection<User> owners = emptyList();
}