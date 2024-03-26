package com.cosmetica.bank.repository;

import com.cosmetica.bank.model.Account;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository implements CrudInterface<Account, String> {
    private final Connection connection;

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Account save(Account entity) {
        String sql = "INSERT INTO accounts (first_name, last_name, account_number, balance, monthly_salary, date_of_birth, allows_overdraft, overdraft_limit, overdraft_interest_rate, loan_interest, bank_name) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getAccountNumber());
            statement.setBigDecimal(4, entity.getBalance());
            statement.setBigDecimal(5, entity.getMonthlySalary());
            statement.setObject(6, entity.getDateOfBirth());
            statement.setBoolean(7, entity.isAllowsOverdraft());
            statement.setBigDecimal(8, entity.getOverdraftLimit());
            statement.setBigDecimal(9, entity.getOverdraftInterestRate());
            statement.setBigDecimal(10, entity.getLoanInterest());
            statement.setString(11, entity.getBankName());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setAccountNumber(generatedKeys.getString(1)); // Set the generated account number
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving account.", ex);
        }
        return entity;
    }

    public Account findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToAccount(resultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding account by account number.", ex);
        }
        return null;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(mapToAccount(resultSet));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all accounts.", ex);
        }
        return accounts;
    }

    @Override
    public Account update(Account entity) {
        String sql = "UPDATE accounts SET first_name = ?, last_name = ?, balance = ?, monthly_salary = ?, date_of_birth = ?, allows_overdraft = ?, overdraft_limit = ?, overdraft_interest_rate = ?, loan_interest = ?, bank_name = ? WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setBigDecimal(3, entity.getBalance());
            statement.setBigDecimal(4, entity.getMonthlySalary());
            statement.setDate(5, entity.getDateOfBirth());
            statement.setBoolean(6, entity.isAllowsOverdraft());
            statement.setBigDecimal(7, entity.getOverdraftLimit());
            statement.setBigDecimal(8, entity.getOverdraftInterestRate());
            statement.setBigDecimal(9, entity.getLoanInterest());
            statement.setString(10, entity.getBankName());
            statement.setString(11, entity.getAccountNumber());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating account failed, no rows affected.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error updating account.", ex);
        }
        return entity;
    }

    @Override
    public void delete(Account entity) {
        deleteById(entity.getAccountNumber());
    }

    @Override
    public void deleteById(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting account failed, no rows affected.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting account.", ex);
        }
    }

    private Account mapToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setFirstName(resultSet.getString("first_name"));
        account.setLastName(resultSet.getString("last_name"));
        account.setBalance(resultSet.getBigDecimal("balance"));
        account.setMonthlySalary(resultSet.getBigDecimal("monthly_salary"));
        account.setDateOfBirth(resultSet.getDate("date_of_birth"));
        account.setAllowsOverdraft(resultSet.getBoolean("allows_overdraft"));
        account.setOverdraftLimit(resultSet.getBigDecimal("overdraft_limit"));
        account.setOverdraftInterestRate(resultSet.getBigDecimal("overdraft_interest_rate"));
        account.setLoanInterest(resultSet.getBigDecimal("loan_interest"));
        account.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        account.setBankName(resultSet.getString("bank_name"));
        return account;
    }
}

