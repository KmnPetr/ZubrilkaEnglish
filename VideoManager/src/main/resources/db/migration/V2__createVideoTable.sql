CREATE TABLE video (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_name VARCHAR NOT NULL,
    bytes BYTEA NOT NULL
);