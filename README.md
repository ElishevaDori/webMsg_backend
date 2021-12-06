# rest-server

schema name: ashCollege

-- auto-generated definition
create table posts
(
    id            int auto_increment,
    content       longtext null,
    creation_date datetime null,
    author_id     int      null,
    constraint posts_id_uindex
        unique (id),
    constraint posts_users_id_fk
        foreign key (author_id) references users (id)
);

alter table posts
    add primary key (id);



-- auto-generated definition
create table users
(
    id       int auto_increment,
    username longtext null,
    password longtext null,
    token    longtext null,
    constraint table_name_id_uindex
        unique (id)
);

alter table users
    add primary key (id);

