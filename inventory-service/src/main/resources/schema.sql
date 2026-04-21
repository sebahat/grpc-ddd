DROP TABLE IF EXISTS inventory_items;

CREATE TABLE inventory_items (
       id BIGSERIAL PRIMARY KEY,
       product_id VARCHAR(255) UNIQUE NOT NULL,
       quantity INT NOT NULL
);