--liquibase formatted sql
--changeset Minich:2
CREATE TABLE IF NOT EXISTS security.roles
(
    id         UUID PRIMARY KEY,
    created_by UUID,
    updated_by UUID REFERENCES security.users (id),
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at TIMESTAMP            DEFAULT now(),
    name       VARCHAR(40) NOT NULL
);