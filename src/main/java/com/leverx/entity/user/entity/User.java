package com.leverx.entity.user.entity;

import com.leverx.entity.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

import static java.util.Collections.emptyList;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column
    @NonNull
    @GeneratedValue(strategy = IDENTITY)
    int id;

    @Column
    @NonNull
    String name;

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "user_pet",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "petId")})
    Collection<Pet> pets = emptyList();
}