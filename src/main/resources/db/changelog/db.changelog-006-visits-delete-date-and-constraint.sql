ALTER TABLE visits
DROP COLUMN scheduled_date_time;

ALTER TABLE visits
DROP CONSTRAINT uk_visits_slot;