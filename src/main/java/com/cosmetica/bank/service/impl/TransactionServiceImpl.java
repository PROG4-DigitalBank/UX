// TransactionServiceImpl.java
package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.model.Account;
import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.repository.TransactionRepository;
import com.cosmetica.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean isWithdrawalAllowed(Account account, BigDecimal amount) {
        // Check if withdrawal is permitted based on available funds and authorized credit
        if (account.isAllowsOverdraft()) {
            BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
            return availableBalance.compareTo(amount) >= 0;
        } else {
            return account.getBalance().compareTo(amount) >= 0;
        }
    }

    @Override
    public void processWithdrawal(Account account, BigDecimal amount) {
        // Proceed with withdrawal and record the transaction
        if (isWithdrawalAllowed(account, amount)) {
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            Transaction withdrawalTransaction = new Transaction();
            withdrawalTransaction.setAccountId(account.getAccountId());
            withdrawalTransaction.setAmount(amount);
            withdrawalTransaction.setTransactionType("Withdrawal");
            withdrawalTransaction.setTransactionReason("Withdrawal from account");

            transactionRepository.save(withdrawalTransaction);
        } else {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }
    }
}
