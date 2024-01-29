
INSERT INTO category (id, name, parent_category_id) VALUES (1, 'Drinks', NULL);
INSERT INTO category (id, name, parent_category_id) VALUES (2, 'Softs', 1);
INSERT INTO category (id, name, parent_category_id) VALUES (3, 'Soda', 2);
INSERT INTO category (id, name, parent_category_id) VALUES (4, 'Tea', 2);
INSERT INTO category (id, name, parent_category_id) VALUES (5, 'Alcohol', 1);

INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
    VALUES (1, 'Cola', 'Sugary black drink', 0.500, 3, 'cola.jpg', 100, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
    VALUES (2, 'Fanta', 'Sugary yellow drink', 0.400, 3, 'fanta.jpg', 120, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
    VALUES (3, 'Black tea', 'Black natural tea', 0.300, 4, 'blacktea.jpg', 80, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO item (id, name, description, price, category_id, picture_url, weight, stock_amount, created_date, last_modified_date)
    VALUES (4, 'Wine', 'An alcoholic drink made from fermented fruit.', 0.800, 5, 'wine.jpg', 30, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO app_user (id, first_name, last_name, login, password)
VALUES (1, 'Aziz', 'Abdukarimov', 'azizdev', '$2a$10$0wkdHjGwk/MR75aFYKxyN.w1kzZY6pnmQrfp.4XoebRLAZuFsplCO');
INSERT INTO app_user (id, first_name, last_name, login, password)
VALUES (2, 'Bob', 'Martin', 'bob', '$2a$10$lsxyv7xQbeyGdtKAOwF93.C0Y1y4/Y83iay2thyBBJU0BK7NO7W1C');

INSERT INTO basket_item (user_id, item_id, amount) VALUES (1, 1, 10);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (1, 2, 20);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (1, 3, 30);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (1, 1, 50);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (2, 3, 20);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (2, 3, 30);
INSERT INTO basket_item (user_id, item_id, amount) VALUES (2, 1, 50);