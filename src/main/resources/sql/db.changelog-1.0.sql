--liquibase formatted sql

--changeset aziz:1

CREATE TABLE item (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(8, 3) NOT NULL,
    category TEXT NOT NULL,
    picture_url TEXT NOT NULL,
    weight INTEGER NOT NULL,
    stock_amount INTEGER NOT NULL,
    created_date TIMESTAMPTZ,
    last_modified_date TIMESTAMPTZ
);

