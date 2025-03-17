CREATE TABLE Person(
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    password varchar NOT NULL,
    username varchar(100) UNIQUE NOT NULL,
    short_name varchar(100),
    sex varchar(50),
    role varchar(100) NOT NULL,
    created_at timestamp,
    rating_voices varchar
);

CREATE TABLE video_info (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cn_name VARCHAR(255),
    en_name VARCHAR(255),
    ru_name VARCHAR(255),
    native_lang VARCHAR(50),
    used_languages VARCHAR(200),
    link_original VARCHAR(500),
    translator_uuid UUID REFERENCES person(uuid) ON DELETE SET NULL
);

CREATE TABLE video (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    video_info_uuid UUID NOT NULL UNIQUE REFERENCES video_info (uuid) ON DELETE CASCADE,
    local_link VARCHAR(1000)
);

CREATE TABLE translation (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    video_info_uuid UUID NOT NULL UNIQUE REFERENCES video_info (uuid) ON DELETE CASCADE,
    version BIGINT DEFAULT 0,
    phrases TEXT
);

CREATE TABLE icon (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    video_info_uuid UUID NOT NULL UNIQUE REFERENCES video_info (uuid) ON DELETE CASCADE,
    bytes BYTEA NOT NULL
);
