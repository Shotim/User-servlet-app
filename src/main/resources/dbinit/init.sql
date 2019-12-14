CREATE DATABASE IF NOT EXISTS 'servlet app';
USE `servlet app`;
create table IF NOT EXISTS pets
(
    id          int auto_increment
        primary key,
    name        varchar(60) not null,
    dateOfBirth date        not null
);

create table IF NOT EXISTS cats
(
    catId            int null,
    miceCaughtNumber int not null,
    constraint cats_pets_id_fk
        foreign key (catId) references pets (id)
            on update cascade on delete cascade
);

create table IF NOT EXISTS dogs
(
    dogId     int        null,
    isCutEars tinyint(1) not null,
    constraint dogs_pets_id_fk
        foreign key (dogId) references pets (id)
            on update cascade on delete cascade
);

create table IF NOT EXISTS users
(
    id   int auto_increment
        primary key,
    name varchar(60) not null
);

create table IF NOT EXISTS user_pet
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