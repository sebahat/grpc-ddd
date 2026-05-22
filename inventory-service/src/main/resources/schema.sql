DROP TABLE IF EXISTS processed_events;
DROP TABLE IF EXISTS inventory_items;

CREATE TABLE inventory_items (
    id BIGSERIAL PRIMARY KEY,
    product_id VARCHAR(255) UNIQUE NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    last_event_version BIGINT
);

CREATE TABLE processed_events (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(255) NOT NULL UNIQUE
);