package com.cosmetica.bank.service;

import com.cosmetica.bank.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    String deposit(String accountNumber, BigDecimal amount);

    String withdraw(String accountNumber, BigDecimal amount);

    String transfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount);
    String scheduleTransfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, LocalDateTime effectiveDateTime);

    String cancelScheduledTransfer(Long transactionId);

    BigDecimal calculateLoansAmount(String accountNumber);

    BigDecimal calculateInterestOnLoans(String accountNumber);

    List<Transaction> getTransactionsByAccountNumber(String accountNumber);
}
