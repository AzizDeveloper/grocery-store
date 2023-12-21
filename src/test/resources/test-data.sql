
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