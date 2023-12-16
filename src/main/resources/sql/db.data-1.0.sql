--liquibase formatted sql

--changeset aziz:2

INSERT INTO item (name, description, price, category, picture_url, weight, stock_amount, created_date, last_modified_date)
VALUES
    ('Apple', 'A fresh and juicy apple', 0.50, 'Fruits', 'apple.jpg', 100, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Banana', 'A ripe and sweet banana', 0.40, 'Fruits', 'banana.jpg', 120, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Carrot', 'A crunchy and healthy carrot', 0.30, 'Vegetables', 'carrot.jpg', 80, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Milk', 'A carton of fresh milk', 1.00, 'Dairy', 'milk.jpg', 1000, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Cheese', 'A block of tasty cheese', 2.00, 'Dairy', 'cheese.jpg', 500, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

