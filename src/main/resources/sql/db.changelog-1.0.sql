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

--changeset aziz:11
CREATE TABLE IF NOT EXISTS promotion_config (
    id SERIAL PRIMARY KEY,
    promotion_type TEXT NOT NULL,
    minimum_amount BIGINT NOT NULL,
    free_amount BIGINT NOT NULL,
    created_date TIMESTAMPTZ,
    end_date TIMESTAMPTZ
);
--rollback drop table promotion_config;

--changeset aziz:12
CREATE TABLE IF NOT EXISTS user_promotion (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    promotion_config_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (promotion_config_id) REFERENCES promotion_config(id)
);
--rollback drop table user_promotion;

--changeset aziz:15
CREATE TABLE IF NOT EXISTS item_promotion (
    id SERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL,
    promotion_config_id BIGINT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES item(id),
    FOREIGN KEY (promotion_config_id) REFERENCES promotion_config(id)
);
--rollback drop table item_promotion;

--changeset aziz:17
TRUNCATE TABLE item_promotion;
DROP TABLE item_promotion;
--rollback create table item_promotion;

--changeset aziz:18
ALTER TABLE promotion_config
ADD COLUMN item_id BIGINT,
ADD CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES item(id);
--rollback alter table promotion_config drop column item_id, drop constraint fk_item_id;

--changeset aziz:21
ALTER TABLE promotion_config ALTER COLUMN free_amount TYPE double precision;

--changeset aziz:22
ALTER TABLE promotion_config ALTER COLUMN free_amount TYPE BIGINT;
