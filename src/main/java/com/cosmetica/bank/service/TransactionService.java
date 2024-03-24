// TransactionService.java
package com.cosmetica.bank.service;

import com.cosmetica.bank.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    String deposit(Long accountId, BigDecimal amount);

    String withdraw(Long accountId, BigDecimal amount);

    String transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount);
    String scheduleTransfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount, LocalDateTime effectiveDateTime);

    String cancelScheduledTransfer(Long transactionId);

    BigDecimal calculateLoansAmount(Long accountId);

    BigDecimal calculateInterestOnLoans(Long accountId);

    List<Transaction> getTransactionsByAccountId(Long accountId);
}