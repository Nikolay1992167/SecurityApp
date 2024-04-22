--liquibase formatted sql
--changeset Minich:1

CREATE TABLE IF NOT EXISTS security.users (
    id           UUID PRIMARY KEY   NOT NULL,
    created_by   UUID,
    updated_by   UUID REFERENCES security.users(id),
    created_at   TIMESTAMP          NOT NULL,
    updated_at   TIMESTAMP,
    first_name   VARCHAR(40)        NOT NULL,
    last_name    VARCHAR(50)        NOT NULL,
    password     VARCHAR(100) UNIQUE NOT NULL,
    email        VARCHAR(100) UNIQUE NOT NULL,
    status       VARCHAR(10)        NOT NULL
);