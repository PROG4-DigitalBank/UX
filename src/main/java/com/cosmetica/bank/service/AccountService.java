// AccountService.java
package com.cosmetica.bank.service;

import com.cosmetica.bank.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);

    Account updateAccount(Account account);

    Account getAccountById(Long accountId);

    List<Account> getAllAccounts();

    BigDecimal getCurrentBalance(Long accountId);

    BigDecimal getCurrentBalanceWithLoansAndInterest(Long accountId);

    void deleteAccount(Long accountId);

    void enableOverdraft(Long accountId);

    void disableOverdraft(Long accountId);

    boolean isOverdraftEnabled(Long accountId);

    BigDecimal calculateAllowedCredit(Long accountId);

    void updateOverdraftInterestRates(Long accountId, BigDecimal interestRateFirstSevenDays,
            BigDecimal interestRateAfterSevenDays);
    String getBankName(Long accountId);

}