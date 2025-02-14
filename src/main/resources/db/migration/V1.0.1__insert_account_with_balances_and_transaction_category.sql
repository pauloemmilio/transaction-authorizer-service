INSERT INTO transaction_category (code, name, created_at, updated_at)
VALUES
    ('5411', 'FOOD', NOW(), NOW()),
    ('5412', 'FOOD', NOW(), NOW()),
    ('5811', 'MEAL', NOW(), NOW()),
    ('5812', 'MEAL', NOW(), NOW());

INSERT INTO account (account_id, created_at, updated_at)
VALUES ('123456789', NOW(), NOW());

INSERT INTO balance (account_id, transaction_category_name, available_amount, created_at, updated_at)
VALUES
    ('123456789', 'FOOD', 100, NOW(), NOW()),
    ('123456789', 'MEAL', 100, NOW(), NOW())