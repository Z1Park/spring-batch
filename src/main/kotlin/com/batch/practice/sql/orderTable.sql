CREATE TABLE IF NOT EXISTS
    orders(
        id BIGSERIAL PRIMARY KEY,
        item_id BIGINT NOT NULL,
        quantity INT NOT NULL,
        price INT NOT NULL,
        status VARCHAR(50) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO orders (item_id, quantity, price, status)
VALUES
    (1, 2, 200, 'PENDING'),
    (1, 1, 100, 'PENDING'),
    (2, 5, 500, 'PENDING'),
    (3, 3, 300, 'PENDING'),
    (4, 4, 400, 'PENDING'),
    (4, 2, 200, 'PENDING'),
    (5, 1, 100, 'PENDING'),
    (6, 5, 500, 'DELIVERING'),
    (7, 3, 300, 'DELIVERING'),
    (8, 4, 400, 'DELEVERED'),
    (9, 1, 300, 'DELEVERED'),
    (10, 2, 100, 'DELEVERED');
