CREATE DATABASE IF NOT EXISTS 'servlet_app';
USE `servlet_app`;
create table pets
(
    id          int auto_increment
        primary key,
    dateOfBirth date        not null,
    name        varchar(60) not null
);

create table cats
(
    miceCaughtNumber int not null,
    catId            int not null
        primary key,
    constraint FK596ma3dstf584crppx3u8gx3r
        foreign key (catId) references pets (id)
);

create table dogs
(
    isCutEars bit not null,
    dogId     int not null
        primary key,
    constraint FKji8snsy0t4ie04ckx7h2tahu8
        foreign key (dogId) references pets (id)
);

create table users
(
    id   int auto_increment
        primary key,
    name varchar(255) null
);

create table user_pet
(
    userId int not null,
    petId  int not null,
    constraint FK8fsjevt7w49xgjvw0ee9xx37p
        foreign key (petId) references pets (id),
    constraint FKfe2ayl6txlveaiby5u4p5vnvg
        foreign key (userId) references users (id)
);

