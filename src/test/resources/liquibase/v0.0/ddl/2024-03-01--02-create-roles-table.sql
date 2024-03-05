--liquibase formatted sql
--changeset Minich:2
CREATE TABLE IF NOT EXISTS security.roles (
    id           UUID PRIMARY KEY   NOT NULL,
    created_by   UUID,
    updated_by   UUID REFERENCES security.users(id),
    created_at   TIMESTAMP          NOT NULL,
    updated_at   TIMESTAMP,
    name         VARCHAR(40)        NOT NULL
);