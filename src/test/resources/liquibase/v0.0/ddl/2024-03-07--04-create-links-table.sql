--liquibase formatted sql
--changeset Minich:4
CREATE TABLE IF NOT EXISTS security.links (
    id           UUID PRIMARY KEY   NOT NULL,
    email        VARCHAR(100) UNIQUE NOT NULL,
    created_at   TIMESTAMP          NOT NULL,
    first_name   VARCHAR(40)        NOT NULL,
    last_name    VARCHAR(50)        NOT NULL,
    type_message VARCHAR(20)        NOT NULL
);