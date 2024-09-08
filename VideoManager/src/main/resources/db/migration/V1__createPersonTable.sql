CREATE TABLE Person(
    id BIGSERIAL PRIMARY KEY,
    email varchar(100) UNIQUE NOT NULL,
    password varchar NOT NULL,
    short_name varchar(100),
    role varchar(100) NOT NULL,
    created_at timestamp
);