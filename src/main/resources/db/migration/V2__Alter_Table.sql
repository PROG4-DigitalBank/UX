-- Add the new constraint to check date of birth is at least 21 years ago
ALTER TABLE customers ADD CONSTRAINT check_date_of_birth CHECK (date_of_birth <= CURRENT_DATE - INTERVAL '21 years');