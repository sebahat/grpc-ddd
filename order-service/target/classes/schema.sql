CREATE TABLE IF NOT EXISTS order_items (
    id VARCHAR(255) PRIMARY KEY,
    product_id VARCHAR(255),
    quantity INT,
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS processed_requests (
    idempotency_key VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);