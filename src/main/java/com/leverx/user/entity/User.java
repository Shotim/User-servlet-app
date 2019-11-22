package com.leverx.user.entity;


import com.leverx.cat.entity.Cat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @NonNull
    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    int id;

    @NonNull
    @Column
    String name;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = ALL,
            mappedBy = "owner",
            orphanRemoval = true)
    List<Cat> cats;
}
