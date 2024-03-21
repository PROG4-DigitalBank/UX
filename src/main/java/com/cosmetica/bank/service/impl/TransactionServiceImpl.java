package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.model.Account;
import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.repository.AccountRepository;
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
    @Autowired
    private AccountRepository accountRepository;

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
    public void applyInterestOnOverdraft(Long accountId) {
        // Retrieve details of the account associated with the account ID
        Account account = accountRepository.findById(accountId);

        if (account != null) {
            // Check if overdraft is allowed for this account
            if (account.isAllowsOverdraft()) {
                // Check if the account balance is less than zero (overdraft)
                if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                    // Calculate interest on the overdraft
                    BigDecimal overdraftBalance = account.getBalance().abs();
                    BigDecimal interestRate = account.getOverdraftInterestRate();
                    BigDecimal interest = overdraftBalance.multiply(interestRate);

                    // Add the interest to the account balance
                    account.setBalance(account.getBalance().add(interest));

                    // Save the changes to the account repository
                    accountRepository.save(account);

                    // Record the overdraft interest transaction
                    Transaction transaction = new Transaction();
                    transaction.setAccountId(accountId);
                    transaction.setAmount(interest);
                    transaction.setTransactionType("OVERDRAFT_INTEREST");
                    transactionRepository.save(transaction);
                }
            } else {
                throw new IllegalArgumentException("Overdraft is not allowed for this account.");
            }
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }
}