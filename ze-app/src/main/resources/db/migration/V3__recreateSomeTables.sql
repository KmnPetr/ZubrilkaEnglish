--пересоздадим две таблицы
--Person, потомучто у нее в прошлой версии было int в качестве идентификатора
--и Statistics потомучто это связанная таблица
--добавилась колонка is_temp_prof в таблицу Person
DROP TABLE Statistics;
DROP TABLE Person;
CREATE TABLE Person(
    id BIGSERIAL PRIMARY KEY,
    email varchar(100) UNIQUE NOT NULL,
    password varchar NOT NULL,
    short_name varchar(100),
    role varchar(100) NOT NULL,
    created_at timestamp,
    is_temp_prof BOOLEAN DEFAULT FALSE
);
CREATE TABLE statistics (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL UNIQUE REFERENCES Person(id) ON DELETE CASCADE,
    points BIGINT,
    last_entry TIMESTAMP,
    new_points integer,
    offline_points integer
);