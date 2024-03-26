package com.cosmetica.bank.config;

import com.cosmetica.bank.repository.AccountRepository;
import com.cosmetica.bank.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

@Configuration
public class RepositoryConfig {

    private final Connection connection; // Inject your database connection here

    public RepositoryConfig(Connection connection) {
        this.connection = connection;
    }

    @Bean
    public AccountRepository accountRepository() {
        return new AccountRepository(connection);
    }

}
