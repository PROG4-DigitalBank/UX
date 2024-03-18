package com.cosmetica.bank.repository;

import com.cosmetica.bank.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements CrudInterface<Customer, Long> {

    private final Connection connection;

    public CustomerRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Customer save(Customer entity) {
        String sql = "INSERT INTO customers (first_name, last_name, email, password, phone_number) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getPhoneNumber());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setCustomerId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving customer.", ex);
        }
        return entity;
    }

    @Override
    public Customer findById(Long id) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToCustomer(resultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding customer by ID.", ex);
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customers.add(mapToCustomer(resultSet));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all customers.", ex);
        }
        return customers;
    }

    @Override
    public Customer update(Customer entity) {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, password = ?, phone_number = ? WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getPhoneNumber());
            statement.setLong(6, entity.getCustomerId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating customer failed, no rows affected.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error updating customer.", ex);
        }
        return entity;
    }

    @Override
    public void delete(Customer entity) {
        deleteById(entity.getCustomerId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting customer failed, no rows affected.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting customer.", ex);
        }
    }

    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToCustomer(resultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding customer by email.", ex);
        }
        return null;
    }

    private Customer mapToCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getLong("customer_id"));
        customer.setFirstName(resultSet.getString("first_name"));
        customer.setLastName(resultSet.getString("last_name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPassword(resultSet.getString("password"));
        customer.setPhoneNumber(resultSet.getString("phone_number"));
        return customer;
    }
}
