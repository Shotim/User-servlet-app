create database if not exists users;
use users;
create table if not exists users
(
    id   int auto_increment
        primary key,
    name text null
);