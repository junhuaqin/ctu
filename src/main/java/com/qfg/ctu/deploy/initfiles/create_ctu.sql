drop database if exists ctu;

create database ctu character set=utf8;

use ctu;

create table roles
(
    id int not null auto_increment primary key,

    name varchar(15) not null,
    permissions int not null default 0,
    description varchar(1024) not null default '',
) ENGINE=INNODB;

create table accounts
(
    id int not null auto_increment primary key,

    username varchar(15) not null,
    password varchar(255) not null default '',
    createdOn timestamp not null default now(),
    lastLoginOn timestamp not null default now(),
    role_id int not null,
    foreign key (role_id) references roles(id)
) ENGINE=INNODB;

insert into roles values (1, 'administrator',0, 'Administrator can do everything');
insert into accounts(username, password, role_id) values ('admin', 'admin', 1);
