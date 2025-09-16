-- Liquibase formatted sql
-- DBMS: postgres
-- Fail: Check database connection or permissions. For extension errors, ensure PostgreSQL version supports pgcrypto.
-- Labels: table,user

-- Changeset adsimka:00101
-- Comment: Add pgcrypto extension for backward compatibility (PostgreSQL < 13)
-- Date: 2025-09-16
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Changeset adsimka:00102
-- Comment: Create table for users
-- Date: 2025-09-16
CREATE TABLE IF NOT EXISTS public.users
(
    id              UUID DEFAULT gen_random_uuid(), -- Generated UUID primary key
    username        VARCHAR(255) NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

-- Changeset adsimka:00103
-- Comment: Create table for user password
-- Date: 2025-09-16
CREATE TABLE IF NOT EXISTS public.user_password
(
    id              UUID DEFAULT gen_random_uuid(), -- Generated UUID primary key
    password        TEXT NOT NULL,
    user_id         UUID NOT NULL UNIQUE,
    CONSTRAINT user_password_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_password_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

-- Changeset adsimka:00104
-- Comment: Create table for user authority
-- Date: 2025-09-16
CREATE TABLE IF NOT EXISTS public.user_authority
(
    id              UUID DEFAULT gen_random_uuid(), -- Generated UUID primary key
    authority       VARCHAR(20) NOT NULL,
    user_id         UUID NOT NULL,
    CONSTRAINT user_authority_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_authority_user FOREIGN KEY (user_id) REFERENCES public.users(id)
);

-- Changeset adsimka:00105
-- Comment: Create index by user_id
-- Date: 2025-09-16
CREATE INDEX IF NOT EXISTS idx_user_password_user_id ON public.user_password(user_id);
CREATE INDEX IF NOT EXISTS idx_user_authority_user_id ON public.user_authority(user_id);
