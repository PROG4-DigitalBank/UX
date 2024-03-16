-- Table for storing information about bank customers
CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(255),
    monthly_salary DECIMAL(15, 2) NOT NULL 
);

-- Table for storing account information
CREATE TABLE accounts (
    account_id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES customers(customer_id),
    account_number VARCHAR(20) UNIQUE NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.00,
    allows_overdraft BOOLEAN DEFAULT FALSE,
    overdraft_limit DECIMAL(15, 2) DEFAULT 0.00,
    overdraft_interest_rate DECIMAL(5, 2),
    loan_interest DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing transaction history
CREATE TABLE transactions (
    transaction_id SERIAL PRIMARY KEY,
    account_id INT REFERENCES accounts(account_id),
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_reason VARCHAR(255)
);

-- Table for storing monthly savings
CREATE TABLE savings (
    savings_id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES customers(customer_id),
    month DATE NOT NULL,
    amount DECIMAL(15, 2) NOT NULL
);
