--liquibase formatted sql
--changeset Minich:4
CREATE TABLE IF NOT EXISTS security.user_tokens
(
    id            UUID PRIMARY KEY,
    created_by    UUID             NOT NULL REFERENCES security.users (id),
    expiration_at TIMESTAMP        NOT NULL,
    token         TEXT             NOT NULL,
    token_type    VARCHAR(50)      NOT NULL
);