INSERT INTO transaction_category (code, name, is_default, created_at, updated_at)
VALUES
    (null, 'CASH', 1, NOW(), NOW()),
    ('5411', 'FOOD', 0, NOW(), NOW()),
    ('5412', 'FOOD', 0, NOW(), NOW()),
    ('5811', 'MEAL', 0, NOW(), NOW()),
    ('5812', 'MEAL', 0, NOW(), NOW());

INSERT INTO account (account_id, created_at, updated_at)
VALUES ('123456789', NOW(), NOW());

INSERT INTO balance (account_id, transaction_category_name, available_amount, created_at, updated_at)
VALUES
    ('123456789', 'CASH', 100, NOW(), NOW()),
    ('123456789', 'FOOD', 100, NOW(), NOW()),
    ('123456789', 'MEAL', 100, NOW(), NOW());

INSERT INTO merchant (merchant, transaction_category_name, created_at, updated_at)
VALUES
    ('UBER TRIP                   SAO PAULO BR', 'CASH', NOW(), NOW()),
    ('UBER EATS                   SAO PAULO BR', 'FOOD', NOW(), NOW()),
    ('UBER EATS                   SAO PAULO BR', 'MEAL', NOW(), NOW()),
    ('PAG*JoseDaSilva          RIO DE JANEI BR', 'MEAL', NOW(), NOW()),
    ('PICPAY*BILHETEUNICO           GOIANIA BR', 'CASH', NOW(), NOW());