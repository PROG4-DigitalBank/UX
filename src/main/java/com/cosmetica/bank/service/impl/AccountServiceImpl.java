// AccountServiceImpl.java
package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.model.Account;
import com.cosmetica.bank.repository.AccountRepository;
import com.cosmetica.bank.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        account.setBalance(BigDecimal.ZERO);
        account.setAllowsOverdraft(false);
        account.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account);
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
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
            account.setOverdraftInterestRate(interestRateFirstSevenDays);
            account.setOverdraftInterestRate(interestRateAfterSevenDays);
            accountRepository.update(account);
        } else {
            throw new IllegalArgumentException("Account not found");
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
    public boolean isOverdraftEnabled(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account != null) {
            return account.isAllowsOverdraft();
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }
}