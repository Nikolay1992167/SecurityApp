--liquibase formatted sql
--changeset Minich:3
INSERT INTO security.user_roles (user_id, role_id)
VALUES ('44212253-a305-4495-9982-45e833aa74ac', '73c65923-b5b1-42df-bd99-299180f287e0'),
       ('f2361e91-718e-41ad-9ddc-4be05ebc09b5', '2512c298-6a1d-48d7-a12d-b51069aceb08'),
       ('b3afa636-8006-42fe-961e-21ae926b3265', '2512c298-6a1d-48d7-a12d-b51069aceb08'),
       ('42f77a57-3cd7-4e5d-a2f8-78f951fc9a41', 'f5b50fda-f157-4a8b-948c-6705206c81c6'),
       ('3a472b53-236d-4cd9-a9d3-0d413ad3b903', 'f5b50fda-f157-4a8b-948c-6705206c81c6');