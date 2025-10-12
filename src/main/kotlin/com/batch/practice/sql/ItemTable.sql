CREATE TABLE IF NOT EXISTS
    items(
         id BIGSERIAL PRIMARY KEY,
         name VARCHAR(255) NOT NULL,
        status VARCHAR(50) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO items (id, name, status)
VALUES
    (1, 'Item 1', 'READY'),
    (2, 'Item 2', 'READY'),
    (3, 'Item 3', 'READY'),
    (4, 'Item 4', 'READY'),
    (5, 'Item 5', 'READY'),
    (6, 'Item 6', 'PROCESSING'),
    (7, 'Item 7', 'PROCESSING'),
    (8, 'Item 8', 'COMPLETED'),
    (9, 'Item 9', 'COMPLETED'),
    (10, 'Item 10', 'COMPLETED');