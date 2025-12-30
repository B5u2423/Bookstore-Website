-- 1. Add tsvector column
ALTER TABLE books
    ADD COLUMN IF NOT EXISTS search_vector tsvector;

-- 2. Backfill existing rows
UPDATE books
SET search_vector =
        to_tsvector(
                'simple',
                coalesce(title, '') || ' ' || coalesce(url_slug, '')
        );

-- 3. Create GIN index
CREATE INDEX IF NOT EXISTS idx_book_search_vector
    ON books
        USING GIN (search_vector);

-- 4. Trigger function
CREATE OR REPLACE FUNCTION book_search_vector_update()
    RETURNS trigger AS $$
BEGIN
    NEW.search_vector :=
            to_tsvector(
                    'simple',
                    coalesce(NEW.title, '') || ' ' || coalesce(NEW.url_slug, '')
            );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 5. Trigger
DROP TRIGGER IF EXISTS book_search_vector_trigger ON books;

CREATE TRIGGER book_search_vector_trigger
    BEFORE INSERT OR UPDATE
    ON books
    FOR EACH ROW
EXECUTE FUNCTION book_search_vector_update();
