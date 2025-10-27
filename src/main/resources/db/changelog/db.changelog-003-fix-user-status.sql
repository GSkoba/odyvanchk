CREATE TYPE user_status_new AS ENUM ('ACTIVE', 'INACTIVE', 'BANNED');

ALTER TABLE users ALTER COLUMN status DROP DEFAULT;

ALTER TABLE users
    ALTER COLUMN status TYPE user_status_new
    USING UPPER(status::text)::user_status_new;

ALTER TABLE users ALTER COLUMN status SET DEFAULT 'ACTIVE';

DROP TYPE user_status;

ALTER TYPE user_status_new RENAME TO user_status;
