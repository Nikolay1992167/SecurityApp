--liquibase formatted sql
--changeset Minich:2
INSERT INTO security.roles (id, created_by, updated_by, created_at, updated_at, name)
VALUES ('73c65923-b5b1-42df-bd99-299180f287e0', NULL, NULL, '2024-01-10T12:00:00.000', '2024-01-11T12:00:00.000', 'ADMIN'),
       ('2512c298-6a1d-48d7-a12d-b51069aceb08', NULL, NULL, '2024-01-10T12:00:00.000', '2024-01-11T12:00:00.000', 'JOURNALIST'),
       ('f5b50fda-f157-4a8b-948c-6705206c81c6', NULL, NULL, '2024-01-10T12:00:00.000', '2024-01-11T12:00:00.000', 'SUBSCRIBER');