package com.cosmetica.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cosmetica.bank.model.Account;

public class AccountRepository implements CrudInterface<Account, Long> {
    private final Connection connection;

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Account save(Account entity) {
        String sql = "INSERT INTO accounts (customer_id, account_number, balance, monthly_salary, allows_overdraft, overdraft_limit, overdraft_interest_rate, loan_interest) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getCustomerId());
            statement.setString(2, entity.getAccountNumber());
            statement.setBigDecimal(3, entity.getBalance());
            statement.setBigDecimal(4, entity.getMonthlySalary());
            statement.setBoolean(5, entity.isAllowsOverdraft());
            statement.setBigDecimal(6, entity.getOverdraftLimit());
            statement.setBigDecimal(7, entity.getOverdraftInterestRate());
            statement.setBigDecimal(8, entity.getLoanInterest());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setAccountId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return entity;
    }

    @Override
    public Account findById(Long id) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToAccount(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account update(Account entity) {
        String sql = "UPDATE accounts SET customer_id = ?, account_number = ?, balance = ?, monthly_salary = ?, allows_overdraft = ?, overdraft_limit = ?, overdraft_interest_rate = ?, loan_interest = ? WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getCustomerId());
            statement.setString(2, entity.getAccountNumber());
            statement.setBigDecimal(3, entity.getBalance());
            statement.setBigDecimal(4, entity.getMonthlySalary());
            statement.setBoolean(5, entity.isAllowsOverdraft());
            statement.setBigDecimal(6, entity.getOverdraftLimit());
            statement.setBigDecimal(7, entity.getOverdraftInterestRate());
            statement.setBigDecimal(8, entity.getLoanInterest());
            statement.setLong(9, entity.getAccountId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating account failed, no rows affected.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return entity;
    }

    @Override
    public void delete(Account entity) {
        deleteById(entity.getAccountId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToAccount(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Account> findByCustomerId(Long customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(mapToAccount(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return accounts;
    }

    private Account mapToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountId(resultSet.getLong("account_id"));
        account.setCustomerId(resultSet.getLong("customer_id"));
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setBalance(resultSet.getBigDecimal("balance"));
        account.setMonthlySalary(resultSet.getBigDecimal("monthly_salary"));
        account.setAllowsOverdraft(resultSet.getBoolean("allows_overdraft"));
        account.setOverdraftLimit(resultSet.getBigDecimal("overdraft_limit"));
        account.setOverdraftInterestRate(resultSet.getBigDecimal("overdraft_interest_rate"));
        account.setLoanInterest(resultSet.getBigDecimal("loan_interest"));
        account.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        return account;
    }
}
