// AccountServiceImpl.java
package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.model.Account;
import com.cosmetica.bank.repository.AccountRepository;
import com.cosmetica.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
@Override
public Optional<Account> getAccountById(Long accountId) {
    Account account = accountRepository.findById(accountId);
    return Optional.ofNullable(account); // Convertir Account en Optional<Account>
}


    @Override
    public BigDecimal calculateAllowedCredit(BigDecimal monthlySalary) {
        return monthlySalary.divide(BigDecimal.valueOf(3)); // ⅓ du salaire mensuel
    }

    @Override
    public boolean isOverdraftAllowed(Account account) {
        return account.isAllowsOverdraft();
    }

    @Override
    public boolean isSufficientFunds(Account account, BigDecimal amount) {
        if (account.isAllowsOverdraft()) {
            BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
            return availableBalance.compareTo(amount) >= 0;
        } else {
            return account.getBalance().compareTo(amount) >= 0;
        }
    }
}
