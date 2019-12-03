create database if not exists users;
use users;
create table if not exists users
(
    id   int auto_increment
        primary key,
    name text not null
);

create table cats
(
    id          int auto_increment
        primary key,
    name        text null,
    ownerId     int  null,
    dateOfBirth date null,
    constraint cats_users_id_fk
        foreign key (ownerId) references users (id)
            on update cascade on delete cascade
);
