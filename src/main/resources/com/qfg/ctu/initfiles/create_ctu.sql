drop database if exists ctu;

create database ctu character set=utf8;

use ctu;

create table roles
(
    id int not null auto_increment primary key,
    name varchar(15) not null,
    permissions int not null default 0,
    description varchar(1024) not null default ''
) ENGINE=INNODB;

create table accounts
(
    id int not null auto_increment primary key,
    name     varchar(10) not null,
    username varchar(15) not null,
    password varchar(256) not null default '',
    createdOn timestamp not null default now(),
    lastLoginOn timestamp not null default now(),
    token varchar(256),
    active TINYINT not null default 1,
    role_id int not null,
    foreign key (role_id) references roles(id)
) ENGINE=INNODB;

create table products
(
    id int not null primary key,
    title varchar(128) not null,
    unitPrice int not null default 0,
    store int not null default 0,
    createdOn timestamp not null default now()
) ENGINE=INNODB;

create table orders
(
    id int not null auto_increment primary key,
    sale int not null,
    createdOn timestamp not null default now(),
    totalPrice int not null default 0,
    foreign key (sale) references accounts(id)
) ENGINE=INNODB;

create table orderItems
(
    order_id int not null,
    product_id int not null,
    unitPrice int not null default 0,
    sale_count int not null default 0,
    foreign key (order_id) references orders(id) ON DELETE CASCADE,
    foreign key (product_id) references products(id) ON DELETE restrict
) ENGINE=INNODB;

insert into roles values (1, 'administrator',0, 'Administrator can do everything');
insert into accounts(name, username, password, role_id) values ('adm', 'admin', 'admin', 1);
insert into accounts(name, username, password, role_id) values ('双群', '15208386955', 'admin', 1);
