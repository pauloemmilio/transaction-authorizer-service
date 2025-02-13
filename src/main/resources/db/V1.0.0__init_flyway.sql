CREATE TABLE account (
    account_id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE transaction_category (
    code VARCHAR(255) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE balance (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    transaction_category_name VARCHAR(255) NOT NULL,
    available_amount DECIMAL(10,2) NOT NULL CHECK (availableAmount >= 0),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_balance_account FOREIGN KEY (account_id) REFERENCES account(account_id),
    CONSTRAINT fk_balance_transactionCategory FOREIGN KEY (transaction_category_name) REFERENCES transaction_category(code),
    CONSTRAINT uq_balance_account_category UNIQUE (account_id, transaction_category_name)
);
