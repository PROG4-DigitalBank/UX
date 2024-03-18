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
        // Vérifier si le retrait est autorisé en fonction des fonds disponibles et du
        // crédit autorisé
        if (account.isAllowsOverdraft()) {
            BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
            return availableBalance.compareTo(amount) >= 0;
        } else {
            return account.getBalance().compareTo(amount) >= 0;
        }
    }

    @Override
    public void processWithdrawal(Account account, BigDecimal amount) {
        // Procéder au retrait et enregistrer la transaction
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
