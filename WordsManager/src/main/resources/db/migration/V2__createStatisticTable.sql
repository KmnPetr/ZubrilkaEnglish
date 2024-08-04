CREATE TABLE statistics (
    id SERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL UNIQUE REFERENCES Person(id) ON DELETE CASCADE,
    points BIGINT,
    last_entry TIMESTAMP,
    new_points integer,
    offline_points integer
);