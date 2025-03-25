-- Включаем расширение pg_trgm (если ещё не включено)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Создаем индекс GIN с триграммами для быстрого текстового поиска по полю text в таблице voice
CREATE INDEX IF NOT EXISTS trgm_idx_voice ON voice USING GIN (text gin_trgm_ops);

-- Создаем индекс GIN с триграммами для быстрого текстового поиска по полю text в таблице card
CREATE INDEX IF NOT EXISTS trgm_idx_card ON card USING GIN (text gin_trgm_ops);