CREATE TABLE video_info (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cn_name VARCHAR(255),
    en_name VARCHAR(255),
    ru_name VARCHAR(255),
    link_original VARCHAR(500),
    translator_uuid UUID REFERENCES person(uuid) ON DELETE SET NULL,
    video_uuid UUID,
    translation_uuid UUID
);