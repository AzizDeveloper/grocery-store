DROP TABLE item IF EXISTS;
DROP TABLE category IF EXISTS;

CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    parent_category_id BIGINT,
    FOREIGN KEY (parent_category_id) REFERENCES category(id)
);

CREATE TABLE item (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(8, 3) NOT NULL,
    category_id BIGINT NOT NULL,
    picture_url TEXT NOT NULL,
    weight INTEGER NOT NULL,
    stock_amount INTEGER NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE,
    last_modified_date TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (category_id) REFERENCES category(id)
);