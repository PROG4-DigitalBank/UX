package com.cosmetica.bank.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConnection {
    @Value("${DB_USERNAME}")
    private String username;
    @Value("${DB_PASSWORD}")
    private String password;
    @Value("${DB_URL}")
    private String dbUrl;

    @Bean
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw new SQLException("Failed to connect to the database.", e);
        }
        return connection;
    }
}
