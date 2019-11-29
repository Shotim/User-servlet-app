package com.leverx.cat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leverx.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "cats")
public class Cat {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    int id;

    @NonNull
    @Column
    String name;

    @NonNull
    @Column
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    Date dateOfBirth;

    @ManyToOne(fetch = EAGER,
            cascade = ALL)
    @JoinColumn(name = "ownerId")
    User owner;
}


