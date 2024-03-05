--liquibase formatted sql
--changeset Minich:1
INSERT INTO security.users (id, created_by, updated_by, created_at, updated_at, first_name, last_name, password, email, status)
VALUES ('44212253-a305-4495-9982-45e833aa74ac', NULL, NULL, '2024-01-01T12:00:00.000', '2024-01-02T12:00:00.000', 'Alex', 'Dronov', '$2a$10$3z6TvAEO2fwLedqVihxZDeQQM.BFCqIc5fP78YUUlL8BcVaPUGHKO', 'dronov@google.com', 'ACTIVE'),
       ('b3afa636-8006-42fe-961e-21ae926b3265', NULL, '44212253-a305-4495-9982-45e833aa74ac', '2024-01-03T12:00:00.000', '2024-01-04T12:00:00.000', 'Nikita', 'Smirnov', '$2a$10$m7IGC.7m1zoDLuQzIsz.O.OKxIeazoy8Sdrkk5Y1JXnqFWfineKhu', 'smirnov@google.com', 'ACTIVE'),
       ('3a472b53-236d-4cd9-a9d3-0d413ad3b903', NULL, '44212253-a305-4495-9982-45e833aa74ac', '2024-01-05T12:00:00.000', '2024-01-06T12:00:00.000', 'Igor', 'Shashkov', '$2a$10$W07EXiI4V40SRRXHZdRBcurorGTysiSsa5h03ZUJmblvctQ.UBW8W', 'shashkov@google.com', 'NOT_ACTIVE');