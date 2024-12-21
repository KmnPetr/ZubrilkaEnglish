CREATE TABLE Person(
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    password varchar NOT NULL,
    username varchar(100) UNIQUE NOT NULL,
    role varchar(100) NOT NULL,
    created_at timestamp
);