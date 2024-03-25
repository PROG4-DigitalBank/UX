CREATE SEQUENCE account_number_sequence START 100000 INCREMENT 1;

-- Table for storing account information
CREATE TABLE accounts (
    account_number VARCHAR(16) PRIMARY KEY DEFAULT ('AC-' || NEXTVAL('account_number_sequence')::text),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.00,
    monthly_salary DECIMAL(15, 2) NOT NULL,
    date_of_birth DATE,
    allows_overdraft BOOLEAN DEFAULT FALSE,
    overdraft_limit DECIMAL(15, 2) DEFAULT 0.00,
    overdraft_interest_rate DECIMAL(5, 2),
    loan_interest DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    bank_name VARCHAR(100)
);

-- Table for storing transaction history
CREATE TABLE transactions (
    transaction_id SERIAL PRIMARY KEY,
    account_number VARCHAR(16) REFERENCES accounts(account_number),
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_reason VARCHAR(255)
);
