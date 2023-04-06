create table author
(
    id serial
        primary key,
    first_name  varchar(255) null,
    second_name varchar(255) null
);

INSERT INTO author (id, first_name, second_name) VALUES (1, 'Ричард', 'Флэнаган');
INSERT INTO author (id, first_name, second_name) VALUES (2, 'Лев', 'Толстой');
INSERT INTO author (id, first_name, second_name) VALUES (3, 'Леонид', 'Андреев');

create table customer
(
    id serial
        primary key,
    first_name  varchar(255) null,
    second_name varchar(255) null
);

INSERT INTO customer (id, first_name, second_name) VALUES (1, 'Дмитрий', 'Дмитриев');
INSERT INTO customer (id, first_name, second_name) VALUES (2, 'Александра', 'Александрова');

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
