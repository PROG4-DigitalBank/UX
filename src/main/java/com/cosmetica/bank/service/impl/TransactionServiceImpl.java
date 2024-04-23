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
    public String deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }

        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Deposit successful.";
    }

    @Override
    public String withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }

        BigDecimal currentBalance = account.getBalance();
        if (currentBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        BigDecimal newBalance = currentBalance.subtract(amount);
        account.setBalance(newBalance);

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount.negate()); // Negative amount for withdrawal
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Withdrawal successful.";
    }

    @Override
    public String transfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
        if (sourceAccount == null || targetAccount == null) {
            throw new IllegalArgumentException("Source or target account not found.");
        }

        BigDecimal currentBalance = sourceAccount.getBalance();
        if (currentBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds for transfer.");
        }

        BigDecimal newSourceBalance = currentBalance.subtract(amount);
        sourceAccount.setBalance(newSourceBalance);
        accountRepository.save(sourceAccount);

        BigDecimal targetBalance = targetAccount.getBalance().add(amount);
        targetAccount.setBalance(targetBalance);
        accountRepository.save(targetAccount);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccountNumber(sourceAccountNumber);
        debitTransaction.setAmount(amount.negate()); // Negative amount for debit
        debitTransaction.setTransactionType("TRANSFER");
        debitTransaction.setTransactionDate(LocalDateTime.now());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccountNumber(targetAccountNumber);
        creditTransaction.setAmount(amount);
        creditTransaction.setTransactionType("TRANSFER");
        creditTransaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return "Transfer successful.";
    }

    @Override
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public BigDecimal calculateLoansAmount(String accountNumber) {
        List<Transaction> loanTransactions = transactionRepository
                .findTransactionsByAccountNumberAndTransactionType(accountNumber, "LOAN");

        BigDecimal totalLoansAmount = BigDecimal.ZERO;

        for (Transaction transaction : loanTransactions) {
            totalLoansAmount = totalLoansAmount.add(transaction.getAmount());
        }

        return totalLoansAmount;
    }

    @Override
    public BigDecimal calculateInterestOnLoans(String accountNumber) {
        List<Transaction> interestTransactions = transactionRepository
                .findTransactionsByAccountNumberAndTransactionType(accountNumber, "LOAN_INTEREST");
        BigDecimal totalInterestOnLoans = BigDecimal.ZERO;
        for (Transaction transaction : interestTransactions) {
            totalInterestOnLoans = totalInterestOnLoans.add(transaction.getAmount());
        }

        return totalInterestOnLoans;
    }

    @Override
    public String scheduleTransfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount,
                                   LocalDateTime effectiveDateTime) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(sourceAccountNumber);
        transaction.setAmount(amount.negate()); // Negative amount for debit
        transaction.setTransactionType("SCHEDULED_TRANSFER");
        transaction.setTransactionDate(effectiveDateTime); // Set the effective date and time
        transaction.setTransactionStatus("PLANNED"); // Set the status as planned

        transactionRepository.save(transaction);

        return "Transfer scheduled successfully.";
    }

    @Override
    public String cancelScheduledTransfer(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId);
        if (transaction != null) {
            transaction.setTransactionStatus("CANCELLED");
            transactionRepository.save(transaction);
            return "Scheduled transfer cancelled successfully.";
        } else {
            throw new IllegalArgumentException("Transaction not found.");
        }
    }
}