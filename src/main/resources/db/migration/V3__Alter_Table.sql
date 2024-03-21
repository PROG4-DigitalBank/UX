-- Add the bankName field to the accounts table
ALTER TABLE accounts
ADD COLUMN bank_name VARCHAR(50);