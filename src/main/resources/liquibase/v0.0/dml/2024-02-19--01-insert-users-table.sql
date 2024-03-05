--liquibase formatted sql
--changeset Minich:1
INSERT INTO security.users (id, created_by, updated_by, created_at, updated_at, first_name, last_name, password, email, status)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', NULL, NULL, '2024-02-15T12:00:00.000', '2024-02-19T12:00:00.000', 'Ivan', 'Sidorov', '$2a$10$ch99apPuJoORMIf8Ew.D9e.cgWa1C6EYQ3iQMp7idTlGyNpyoF.P.', 'ivan@google.com', 'ACTIVE'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', NULL, NULL, '2024-02-13T12:00:00.000', '2024-02-16T12:00:00.000', 'Egor', 'Strelin', '$2a$10$uuwsQHbWZMIUMTuxKwij8e/l5zea9.Q2XW0eG3Bs/2fUMarbqiymG', 'strelin@mail.ru', 'ACTIVE'),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', NULL, NULL, '2024-02-11T12:00:00.000', '2024-02-14T12:00:00.000', 'Alex', 'Volk', '$2a$10$wH5b5g3QibOOdDhOVlSGxuyvqOO4kDcWMI3TQKNK9HdzUeQLowmNG', 'volk@google.com', 'ACTIVE');