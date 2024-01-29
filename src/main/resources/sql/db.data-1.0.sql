--liquibase formatted sql

--changeset aziz:2

INSERT INTO item (name, description, price, category, picture_url, weight, stock_amount, created_date, last_modified_date)
VALUES
    ('Apple', 'A fresh and juicy apple', 0.50, 'Fruits', 'apple.jpg', 100, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Banana', 'A ripe and sweet banana', 0.40, 'Fruits', 'banana.jpg', 120, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Carrot', 'A crunchy and healthy carrot', 0.30, 'Vegetables', 'carrot.jpg', 80, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Milk', 'A carton of fresh milk', 1.00, 'Dairy', 'milk.jpg', 1000, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Cheese', 'A block of tasty cheese', 2.00, 'Dairy', 'cheese.jpg', 500, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

--changeset aziz:5

INSERT INTO category (id, name, parent_category_id) VALUES (1, 'Drinks', NULL);
INSERT INTO category (id, name, parent_category_id) VALUES (2, 'Softs', 1);
INSERT INTO category (id, name, parent_category_id) VALUES (3, 'Soda', 2);
INSERT INTO category (id, name, parent_category_id) VALUES (4, 'Tea', 2);
INSERT INTO category (id, name, parent_category_id) VALUES (5, 'Alcohol', 1);

--changeset aziz:6

INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
VALUES (1, 'Cola', 'Sugary black drink', 0.500, 3, 'cola.jpg', 100, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
VALUES (2, 'Fanta', 'Sugary yellow drink', 0.400, 3, 'fanta.jpg', 120, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
VALUES (3, 'Black tea', 'Black natural tea', 0.300, 4, 'blacktea.jpg', 80, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
VALUES (4, 'Wine', 'An alcoholic drink made from fermented fruit.', 0.800, 5, 'wine.jpg', 30, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

--changeset aziz:9

INSERT INTO app_user (id, first_name, last_name, login, password)
VALUES (2, 'Bruno', 'Fernandes', 'bruno', '$2a$10$0wkdHjGwk/MR75aFYKxyN.w1kzZY6pnmQrfp.4XoebRLAZuFsplCO');
INSERT INTO app_user (id, first_name, last_name, login, password)
VALUES (3, 'Bob', 'Martin', 'bob', '$2a$10$lsxyv7xQbeyGdtKAOwF93.C0Y1y4/Y83iay2thyBBJU0BK7NO7W1C');

INSERT INTO basket_item (user_id, item_id, amount) VALUES (2, 1, 10);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (2, 2, 20);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (2, 3, 30);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (2, 1, 50);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (3, 3, 20);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (3, 3, 30);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (3, 1, 50);

--changeset aziz:10

DELETE FROM basket_item WHERE id = 4;
DELETE FROM basket_item WHERE id = 6;
