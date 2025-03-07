CREATE TABLE account (
    account_id VARCHAR(36) NOT NULL PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE transaction_category (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE balance (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    transaction_category_name VARCHAR(50) NOT NULL,
    available_amount DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (account_id) REFERENCES account(account_id),
    UNIQUE (account_id, transaction_category_name)
);

CREATE TABLE merchant (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    merchant VARCHAR(255) NOT NULL,
    transaction_category_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (merchant, transaction_category_name)
);

CREATE TABLE transaction_history (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_category VARCHAR(50) NOT NULL,
    merchant VARCHAR(255) NOT NULL,
    response_code VARCHAR(255) NOT NULL,
    response_message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);