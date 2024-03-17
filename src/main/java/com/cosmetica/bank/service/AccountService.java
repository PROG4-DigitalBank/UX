// AccountService.java
package com.cosmetica.bank.service;

import com.cosmetica.bank.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountById(Long accountId);

    BigDecimal calculateAllowedCredit(BigDecimal monthlySalary);

    boolean isOverdraftAllowed(Account account);

    boolean isSufficientFunds(Account account, BigDecimal amount);
}
