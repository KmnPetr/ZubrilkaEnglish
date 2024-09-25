CREATE TABLE translation (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT DEFAULT 0,
    phrases TEXT
);