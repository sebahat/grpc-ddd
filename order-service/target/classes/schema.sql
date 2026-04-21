DROP TABLE IF EXISTS inventory_items;

CREATE TABLE inventory_items (
    id VARCHAR(255) PRIMARY KEY,
    product_id VARCHAR(255),
    quantity INT
);