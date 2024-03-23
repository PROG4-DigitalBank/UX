// AccountServiceImpl.java
package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.model.Account;
import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.repository.AccountRepository;
import com.cosmetica.bank.repository.TransactionRepository;
import com.cosmetica.bank.service.AccountService;
import com.cosmetica.bank.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;

    @Override
    public Account createAccount(Account account) {
        try {
            account.setBalance(BigDecimal.ZERO);
            account.setCreatedAt(LocalDateTime.now());

            return accountRepository.save(account);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Erreur lors de la création du compte", ex);
        }
    }

    @Override
    public Account updateAccount(Account account) {
        Account existingAccount = accountRepository.findById(account.getAccountId());
        if (existingAccount == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }

        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        try {
            return accountRepository.findById(accountId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Account not found.");
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public void enableOverdraft(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            account.setAllowsOverdraft(true);
            accountRepository.update(account);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public void disableOverdraft(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            account.setAllowsOverdraft(false);
            accountRepository.update(account);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public BigDecimal calculateAllowedCredit(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            BigDecimal monthlySalary = account.getMonthlySalary();
            return monthlySalary.divide(new BigDecimal(3));
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public void updateOverdraftInterestRates(Long accountId, BigDecimal interestRateFirstSevenDays,
            BigDecimal interestRateAfterSevenDays) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            BigDecimal balance = account.getBalance();
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                // The account is overdraft
                LocalDateTime today = LocalDateTime.now();
                LocalDateTime sevenDaysAfterCreation = account.getCreatedAt().plusDays(7);
                BigDecimal interestRate;
                if (today.isAfter(sevenDaysAfterCreation)) {
                    // After the first seven days
                    interestRate = interestRateAfterSevenDays;
                } else {
                    // Within the first seven days
                    interestRate = interestRateFirstSevenDays;
                }
                // Check if the interest rate has changed
                if (interestRate.compareTo(account.getOverdraftInterestRate()) != 0) {
                    BigDecimal interest = balance.abs().multiply(interestRate);
                    account.setBalance(balance.add(interest));
                    accountRepository.update(account);

                    // Recording the overdraft interest transaction
                    Transaction transaction = new Transaction();
                    transaction.setAccountId(accountId);
                    transaction.setAmount(interest);
                    transaction.setTransactionType("OVERDRAFT_INTEREST");
                    transactionRepository.save(transaction);
                }
            } else {
                // The account is not overdraft
                throw new IllegalArgumentException("Account is not overdraft.");
            }
        } else {
            // Account not found
            throw new IllegalArgumentException("Account not found.");
        }
    }

    @Override
    public BigDecimal getCurrentBalance(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            return account.getBalance();
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public BigDecimal getCurrentBalanceWithLoansAndInterest(Long accountId) {
        BigDecimal principalBalance = getCurrentBalance(accountId);
        BigDecimal loansAmount = transactionService.calculateLoansAmount(accountId);
        BigDecimal interestOnLoans = transactionService.calculateInterestOnLoans(accountId);
        return principalBalance.add(loansAmount).add(interestOnLoans);
    }

    @Override
    public boolean isOverdraftEnabled(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            return account.isAllowsOverdraft();
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }
<<<<<<< HEAD
=======

>>>>>>> 6ee46c0 (fix: Delete bankName method in AccountServiceImpl)
}