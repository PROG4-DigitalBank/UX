package com.cosmetica.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cosmetica.bank.model.Transaction;

public class TransactionRepository implements CrudInterface<Transaction, Long> {

    // Connection to DB
    private final Connection connection;

    public TransactionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaction save(Transaction entity) {
        String sql = "INSERT INTO customers (account_id, amount, transaction_type, transaction_date, transaction_reason) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getAccountId());
            statement.setBigDecimal(2, entity.getAmount());
            statement.setString(3, entity.getTransactionType());
            statement.setObject(4, entity.getTransactionDate());
            statement.setString(5, entity.getTransactionReason());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Transaction insertion failed.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setAccountId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Transaction creation failed, no ID obtained.");
                }
            }
            return entity;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving transaction.", ex);
        }
    }

    @Override
    public Transaction findById(Long id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractTransactionFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding transaction by ID.", ex);
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(extractTransactionFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all transactions.", ex);
        }
        return transactions;
    }

    @Override
    public Transaction update(Transaction entity) {
        String sql = "UPDATE transactions SET account_id = ?, amount = ?, transaction_type = ?, transaction_date = ?, transaction_reason = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getAccountId());
            statement.setBigDecimal(2, entity.getAmount());
            statement.setString(3, entity.getTransactionType());
            statement.setObject(4, entity.getTransactionDate());
            statement.setString(5, entity.getTransactionReason());
            statement.setLong(6, entity.getTransactionId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating transaction failed, no rows affected.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error updating transaction.", ex);
        }
        return entity;
    }

    @Override
    public void delete(Transaction entity) {
        deleteById(entity.getTransactionId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting transaction failed, no rows affected.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting transaction.", ex);
        }
    }

    public List<Transaction> findByAccountId(Long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(extractTransactionFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding transactions by account ID.", ex);
        }
        return transactions;
    }

    public List<Transaction> findTransactionsByAccountIdAndTransactionType(Long accountId, String transactionType) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND transaction_type = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountId);
            statement.setString(2, transactionType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(extractTransactionFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding transactions by account ID and transaction type.", ex);
        }
        return transactions;
    }

    private Transaction extractTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getLong("transaction_id"));
        transaction.setAccountId(resultSet.getLong("account_id"));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setTransactionType(resultSet.getString("transaction_type"));
        transaction.setTransactionDate(resultSet.getObject("transaction_date", LocalDateTime.class));
        transaction.setTransactionReason(resultSet.getString("transaction_reason"));
        return transaction;
    }
}
