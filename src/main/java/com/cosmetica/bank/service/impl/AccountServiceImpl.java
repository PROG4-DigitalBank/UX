package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.model.Account;
import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.repository.AccountRepository;
import com.cosmetica.bank.repository.TransactionRepository;
import com.cosmetica.bank.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cosmetica.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired TransactionService transactionService;

    @Override
    public Account createAccount(Account account) {
        try {
            account.setLoanInterest(BigDecimal.ZERO);
            account.setOverdraftInterestRate(BigDecimal.ZERO);
            account.setOverdraftLimit(BigDecimal.ZERO);
            account.setBalance(BigDecimal.ZERO);
            account.setCreatedAt(LocalDateTime.now());

            return accountRepository.save(account);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Error creating the account", ex);
        }
    }

    @Override
    public Account updateAccount(Account account) {
        Account existingAccount = accountRepository.findByAccountNumber(account.getAccountNumber());
        if (existingAccount != null) {
            return accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("Account does not exist.");
        }
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            return account;
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(String accountNumber) {
        Account accountToDelete = accountRepository.findByAccountNumber(accountNumber);
        if (accountToDelete != null) {
            accountRepository.delete(accountToDelete);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public void enableOverdraft(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.setAllowsOverdraft(true);
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public void disableOverdraft(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.setAllowsOverdraft(false);
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public BigDecimal calculateAllowedCredit(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            BigDecimal monthlySalary = account.getMonthlySalary();
            return monthlySalary.divide(new BigDecimal(3));
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public void updateOverdraftInterestRates(String accountNumber, BigDecimal interestRateFirstSevenDays,
                                             BigDecimal interestRateAfterSevenDays) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
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
                    accountRepository.save(account);

                    // Recording the overdraft interest transaction
                    Transaction transaction = new Transaction();
                    transaction.setAccountNumber(accountNumber);
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
    public BigDecimal getCurrentBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            return account.getBalance();
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public BigDecimal getCurrentBalanceWithLoansAndInterest(String accountNumber) {
        BigDecimal principalBalance = getCurrentBalance(accountNumber);
        BigDecimal loansAmount = transactionService.calculateLoansAmount(accountNumber);
        BigDecimal interestOnLoans = transactionService.calculateInterestOnLoans(accountNumber);
        return principalBalance.add(loansAmount).add(interestOnLoans);
    }

    @Override
    public boolean isOverdraftEnabled(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            return account.isAllowsOverdraft();
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }
}
