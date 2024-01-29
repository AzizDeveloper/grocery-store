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

--changeset aziz:3

CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    parent_category_id BIGINT,
    FOREIGN KEY (parent_category_id) REFERENCES category(id)
);

--changeset aziz:4

TRUNCATE TABLE item;

ALTER TABLE item
DROP COLUMN category,
ADD COLUMN category_id BIGINT NOT NULL,
ADD FOREIGN KEY (category_id) REFERENCES category(id);

--changeset aziz:7
CREATE TABLE IF NOT EXISTS app_user (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
--rollback drop table app_user;

--changeset aziz:8
CREATE TABLE IF NOT EXISTS basket_item (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    amount INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (item_id) REFERENCES item(id)
);
--rollback drop table basket_item;