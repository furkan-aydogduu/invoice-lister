drop table if exists invoice;

create sequence invoice_seq start with 1 increment by 1;

create table invoice (id bigint not null default nextval('invoice_seq') primary key, invoicedate timestamp, price double not null, customer_id bigint);

--alter table invoice add constraint FK5e32ukwo9uknwhylogvta4po6 foreign key (customer_id) references customer;