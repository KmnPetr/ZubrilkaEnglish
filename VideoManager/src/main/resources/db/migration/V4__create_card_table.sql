CREATE TABLE card (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text VARCHAR(1000) NOT NULL,
    transcription VARCHAR(1000),
    translation VARCHAR(2000),
    language VARCHAR(20) NOT NULL,
    level VARCHAR(20),
    voice_uuid UUID
);