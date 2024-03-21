package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.repository.TransactionRepository;
import com.cosmetica.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public String deposit(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Deposit successful.";
    }

    @Override
    public String withdraw(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount.negate()); // Negative amount for withdrawal
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Withdrawal successful.";
    }

    @Override
    public String transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccountId(sourceAccountId);
        debitTransaction.setAmount(amount.negate()); // Negative amount for debit
        debitTransaction.setTransactionType("TRANSFER");
        debitTransaction.setTransactionDate(LocalDateTime.now());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccountId(targetAccountId);
        creditTransaction.setAmount(amount);
        creditTransaction.setTransactionType("TRANSFER");
        creditTransaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return "Transfer successful.";
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Override
    public BigDecimal calculateLoansAmount(Long accountId) {
        List<Transaction> loanTransactions = transactionRepository
                .findTransactionsByAccountIdAndTransactionType(accountId, "LOAN");

        BigDecimal totalLoansAmount = BigDecimal.ZERO;

        for (Transaction transaction : loanTransactions) {
            totalLoansAmount = totalLoansAmount.add(transaction.getAmount());
        }

        return totalLoansAmount;
    }

    @Override
    public BigDecimal calculateInterestOnLoans(Long accountId) {
        List<Transaction> interestTransactions = transactionRepository
                .findTransactionsByAccountIdAndTransactionType(accountId, "LOAN_INTEREST");
        BigDecimal totalInterestOnLoans = BigDecimal.ZERO;
        for (Transaction transaction : interestTransactions) {
            totalInterestOnLoans = totalInterestOnLoans.add(transaction.getAmount());
        }

        return totalInterestOnLoans;
    }
}