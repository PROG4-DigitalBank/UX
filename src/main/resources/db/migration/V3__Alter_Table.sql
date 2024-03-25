-- Add the fields effective_date_time and transaction_status to the transactions table
ALTER TABLE transactions
ADD COLUMN effective_date_time TIMESTAMP,
ADD COLUMN transaction_status VARCHAR(255);