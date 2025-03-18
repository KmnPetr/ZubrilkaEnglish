-- Включаем расширение pg_trgm для нечёткого поиска по тексту
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Создаем индекс GIN с триграммами для быстрого текстового поиска
CREATE INDEX IF NOT EXISTS trgm_idx ON voice USING GIN (text gin_trgm_ops);