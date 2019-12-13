create table pets
(
    id          int auto_increment
        primary key,
    name        varchar(60) not null,
    dateOfBirth date        not null,
    ownerId     int         null
);

create table cats
(
    catId            int null,
    miceCaughtNumber int not null,
    constraint cats_pets_id_fk
        foreign key (catId) references pets (id)
            on update cascade on delete cascade
);

create table dogs
(
    dogId     int        null,
    isCutEars tinyint(1) not null,
    constraint dogs_pets_id_fk
        foreign key (dogId) references pets (id)
            on update cascade on delete cascade
);

create index pets_users_id_fk
    on pets (ownerId);

create table users
(
    id   int auto_increment
        primary key,
    name varchar(60) not null
);

create table user_pet
(
    userId int not null,
    petId  int not null,
    constraint user_pet_pets_id_fk
        foreign key (petId) references pets (id)
            on update cascade on delete cascade,
    constraint user_pet_users_id_fk
        foreign key (userId) references users (id)
            on update cascade on delete cascade
);

