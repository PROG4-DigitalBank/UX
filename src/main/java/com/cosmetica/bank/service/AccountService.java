package com.cosmetica.bank.service;

import com.cosmetica.bank.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);

    Account updateAccount(Account account);

    Account getAccountByAccountNumber(String accountNumber);

    List<Account> getAllAccounts();

    BigDecimal getCurrentBalance(String accountNumber);

    BigDecimal getCurrentBalanceWithLoansAndInterest(String accountNumber);

    void deleteAccount(String accountNumber);

    void enableOverdraft(String accountNumber);

    void disableOverdraft(String accountNumber);

    boolean isOverdraftEnabled(String accountNumber);

    BigDecimal calculateAllowedCredit(String accountNumber);

    void updateOverdraftInterestRates(String accountNumber, BigDecimal interestRateFirstSevenDays,
            BigDecimal interestRateAfterSevenDays);
}
