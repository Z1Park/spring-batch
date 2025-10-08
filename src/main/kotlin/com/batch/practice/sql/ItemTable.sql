CREATE TABLE IF NOT EXISTS items(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO items (name, status) VALUES
('Item 1', 'READY'),
('Item 2', 'READY'),
('Item 3', 'FAILED'),
('Item 4', 'COMPLETED');