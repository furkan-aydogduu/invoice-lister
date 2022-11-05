drop table if exists sys_user;
drop table if exists customer;

create sequence customer_seq start with 1 increment by 1;
create sequence sys_user_seq start with 1 increment by 1;

create table customer (id bigint not null default nextval('customer_seq') primary key, name varchar(255));
create table sys_user (id bigint not null default nextval('sys_user_seq') primary key, name varchar(255), password varchar(255));
