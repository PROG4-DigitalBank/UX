// TransactionService.java
package com.cosmetica.bank.service;

import com.cosmetica.bank.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    String deposit(Long accountId, BigDecimal amount);

    String withdraw(Long accountId, BigDecimal amount);

    String transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount);

    List<Transaction> getTransactionsByAccountId(Long accountId);
}