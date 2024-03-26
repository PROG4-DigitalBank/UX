package com.cosmetica.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cosmetica.bank.model.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository implements CrudInterface<Transaction, Long> {

    // Connection to DB
    private final Connection connection;

    public TransactionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaction save(Transaction entity) {
        String sql = "INSERT INTO transactions (account_number, amount, transaction_type, transaction_date, transaction_reason, effective_date_time, transaction_status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getAccountNumber());
            statement.setBigDecimal(2, entity.getAmount());
            statement.setString(3, entity.getTransactionType());
            statement.setObject(4, entity.getTransactionDate());
            statement.setString(5, entity.getTransactionReason());
            statement.setObject(6, entity.getEffectiveDateTime());
            statement.setString(7, entity.getTransactionStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Transaction insertion failed.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setTransactionId(generatedKeys.getLong(1));
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
        String sql = "UPDATE transactions SET account_number = ?, amount = ?, transaction_type = ?, transaction_date = ?, transaction_reason = ?, effective_date_time = ?, transaction_status = ? WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getAccountNumber());
            statement.setBigDecimal(2, entity.getAmount());
            statement.setString(3, entity.getTransactionType());
            statement.setObject(4, entity.getTransactionDate());
            statement.setString(5, entity.getTransactionReason());
            statement.setObject(6, entity.getEffectiveDateTime());
            statement.setString(7, entity.getTransactionStatus());
            statement.setLong(8, entity.getTransactionId());

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
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
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

    public Transaction findById(Long id) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractTransactionFromResultSet(resultSet);
            } else {
                throw new RuntimeException("Transaction not found with ID: " + id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding transaction by ID.", ex);
        }
    }

    public List<Transaction> findByAccountNumber(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(extractTransactionFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding transactions by account number.", ex);
        }
        return transactions;
    }

    public List<Transaction> findTransactionsByAccountNumberAndTransactionType(String accountNumber, String transactionType) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? AND transaction_type = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.setString(2, transactionType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(extractTransactionFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding transactions by account number and transaction type.", ex);
        }
        return transactions;
    }

    private Transaction extractTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getLong("transaction_id"));
        transaction.setAccountNumber(resultSet.getString("account_number"));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setTransactionType(resultSet.getString("transaction_type"));
        transaction.setTransactionDate(resultSet.getObject("transaction_date", LocalDateTime.class));
        transaction.setTransactionReason(resultSet.getString("transaction_reason"));
        transaction.setEffectiveDateTime(resultSet.getObject("effective_date_time", LocalDateTime.class));
        transaction.setTransactionStatus(resultSet.getString("transaction_status"));
        return transaction;
    }
}
