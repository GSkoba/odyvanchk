-- =====================================================
-- Pet Clinic Database Schema - PostgreSQL
-- =====================================================

-- Drop tables if they exist (for clean recreation)
DROP TABLE IF EXISTS visits CASCADE;
DROP TABLE IF EXISTS vet_slots CASCADE;
DROP TABLE IF EXISTS pets CASCADE;
DROP TABLE IF EXISTS vet_specialties CASCADE;
DROP TABLE IF EXISTS vets CASCADE;
DROP TABLE IF EXISTS specialties CASCADE;
DROP TABLE IF EXISTS owners CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- =====================================================
-- Table: roles
-- =====================================================
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name IN ('ADMIN', 'VET', 'OWNER')),
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Table: users
-- =====================================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Table: user_roles (Many-to-Many)
-- =====================================================
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id)
        REFERENCES roles(id) ON DELETE CASCADE
);

-- =====================================================
-- Table: owners
-- =====================================================
CREATE TABLE owners (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(500),
    city VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_owners_users FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- Table: specialties
-- =====================================================
CREATE TABLE specialties (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Table: vets
-- =====================================================
CREATE TABLE vets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vets_users FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- Table: vet_specialties (Many-to-Many)
-- =====================================================
CREATE TABLE vet_specialties (
    vet_id BIGINT NOT NULL,
    specialty_id INT NOT NULL,
    PRIMARY KEY (vet_id, specialty_id),
    CONSTRAINT fk_vet_specialties_vet FOREIGN KEY (vet_id)
        REFERENCES vets(id) ON DELETE CASCADE,
    CONSTRAINT fk_vet_specialties_specialty FOREIGN KEY (specialty_id)
        REFERENCES specialties(id) ON DELETE CASCADE
);

-- =====================================================
-- Table: vet_slots
-- =====================================================
CREATE TABLE vet_slots (
    id BIGSERIAL PRIMARY KEY,
    vet_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT true,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'BOOKED', 'BLOCKED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vet_slots_vet FOREIGN KEY (vet_id)
        REFERENCES vets(id) ON DELETE CASCADE,
    CONSTRAINT chk_vet_slots_time CHECK (end_time > start_time),
    CONSTRAINT uk_vet_slots_unique UNIQUE (vet_id, start_time)
);

-- =====================================================
-- Table: pets
-- =====================================================
CREATE TABLE pets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL CHECK (birth_date <= CURRENT_DATE),
    type VARCHAR(50) NOT NULL CHECK (type IN ('DOG', 'CAT', 'BIRD', 'HAMSTER', 'RABBIT', 'REPTILE', 'OTHER')),
    owner_id BIGINT NOT NULL,
    breed VARCHAR(100),
    color VARCHAR(50),
    weight DECIMAL(5,2) CHECK (weight > 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pets_owner FOREIGN KEY (owner_id)
        REFERENCES owners(id)
);

-- =====================================================
-- Table: visits
-- =====================================================
CREATE TABLE visits (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    vet_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    scheduled_date_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    description TEXT,
    diagnosis TEXT,
    notes TEXT,
    cancellation_reason VARCHAR(500),
    cancellation_date_time TIMESTAMP,
    completion_date_time TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_visits_pet FOREIGN KEY (pet_id)
        REFERENCES pets(id) ON DELETE CASCADE,
    CONSTRAINT fk_visits_vet FOREIGN KEY (vet_id)
        REFERENCES users(id),
    CONSTRAINT fk_visits_slot FOREIGN KEY (slot_id)
        REFERENCES vet_slots(id),
    CONSTRAINT uk_visits_slot UNIQUE (slot_id)
);