BEGIN;

-- 1️⃣ Add a new temporary column to hold the new vet_id
ALTER TABLE visits
ADD COLUMN vet_new_id BIGINT;

-- 2️⃣ Update the new column using the mapping from vets.user_id
UPDATE visits
SET vet_new_id = v.id
FROM vets v
WHERE v.user_id = visits.vet_id;

-- 3️⃣ Check for unmapped rows (optional safety check)
DO $$
DECLARE missing_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO missing_count
    FROM visits
    WHERE vet_new_id IS NULL;

    IF missing_count > 0 THEN
        RAISE EXCEPTION 'Migration aborted: % visit(s) could not be mapped to a vet.', missing_count;
    END IF;
END $$;

-- 4️⃣ Add foreign key constraint on the new column
ALTER TABLE visits
ADD CONSTRAINT fk_visits_vet_new
FOREIGN KEY (vet_new_id)
REFERENCES vets(id)
ON DELETE CASCADE;

-- 5️⃣ Drop the old foreign key and old column
ALTER TABLE visits
DROP CONSTRAINT IF EXISTS fk_visits_vet;
ALTER TABLE visits
DROP COLUMN vet_id;

-- 6️⃣ Rename the new column to vet_id
ALTER TABLE visits
RENAME COLUMN vet_new_id TO vet_id;

COMMIT;
