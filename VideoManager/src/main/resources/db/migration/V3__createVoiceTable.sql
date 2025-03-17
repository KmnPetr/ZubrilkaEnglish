CREATE TABLE voice (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text VARCHAR(1000),
    voice VARCHAR(100),
    sex VARCHAR(100),
    local_link VARCHAR(500)
);