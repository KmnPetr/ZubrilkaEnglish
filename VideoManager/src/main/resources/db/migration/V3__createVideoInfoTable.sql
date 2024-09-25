CREATE TABLE video_info (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    translator_uuid UUID REFERENCES person(uuid) ON DELETE SET NULL,
    video_uuid UUID,
    translation_uuid UUID
);