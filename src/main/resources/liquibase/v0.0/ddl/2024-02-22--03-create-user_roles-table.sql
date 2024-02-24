--liquibase formatted sql
--changeset Minich:3
CREATE TABLE IF NOT EXISTS security.user_roles (
    user_id UUID NOT NULL REFERENCES security.users(id),
    role_id UUID NOT NULL REFERENCES security.roles(id),
    PRIMARY KEY (user_id, role_id)
);