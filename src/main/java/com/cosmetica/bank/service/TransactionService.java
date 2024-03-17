// TransactionService.java
package com.cosmetica.bank.service;

import com.cosmetica.bank.model.Account;

import java.math.BigDecimal;

public interface TransactionService {
    boolean isWithdrawalAllowed(Account account, BigDecimal amount);

    void processWithdrawal(Account account, BigDecimal amount);
}
