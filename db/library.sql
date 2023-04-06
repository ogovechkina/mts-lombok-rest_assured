create table author
(
    id serial
        primary key,
    first_name  varchar(255) null,
    second_name varchar(255) null
);

INSERT INTO author (first_name, second_name) VALUES ('Ричард', 'Флэнаган');
INSERT INTO author (first_name, second_name) VALUES ('Лев', 'Толстой');
INSERT INTO author (first_name, second_name) VALUES ('Леонид', 'Андреев');

create table customer
(
    id serial
        primary key,
    first_name  varchar(255) null,
    second_name varchar(255) null
);

INSERT INTO customer (first_name, second_name) VALUES ('Дмитрий', 'Дмитриев');
INSERT INTO customer (first_name, second_name) VALUES ('Александра', 'Александрова');

create table book
(
    id serial
        primary key,
    book_title varchar(255) null,
    author_id  bigint       null,
    customer_id    bigint       null,
    constraint FKklnrv3weler2ftkweewlky958
            foreign key (author_id) references author (id)
);
