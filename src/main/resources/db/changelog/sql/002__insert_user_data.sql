-- Liquibase formatted sql
-- DBMS: postgres
-- Fail: Check database connection or permissions. Check for the existence of the users, user_password, and user_authority tables.
-- Labels: insert,user

-- Changeset adsimka:00201
-- Comment: entering data into the user table
-- Date: 2025-09-18
INSERT INTO users(username)
VALUES ('test_user');

-- Changeset adsimka:00202
-- Comment: entering data into the user_password table
-- Date: 2025-09-18
INSERT INTO user_password(password, user_id)
VALUES ('{noop}password', (SELECT id FROM users LIMIT 1));

-- Changeset adsimka:00203
-- Comment: entering data into the user_authority table
-- Date: 2025-09-18
INSERT INTO user_authority(authority, user_id)
VALUES ('ROLE_USER', (SELECT id FROM users LIMIT 1)),
       ('ROLE_ADMIN', (SELECT id FROM users LIMIT 1));