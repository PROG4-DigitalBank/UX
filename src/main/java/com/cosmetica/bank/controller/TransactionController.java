package com.cosmetica.bank.controller;

import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        String message = transactionService.deposit(accountId, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        String message = transactionService.withdraw(accountId, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long sourceAccountId, @RequestParam Long targetAccountId,
            @RequestParam BigDecimal amount) {
        String message = transactionService.transfer(sourceAccountId, targetAccountId, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/applyInterestOnOverdraft")
    public ResponseEntity<String> applyInterestOnOverdraft(@RequestParam Long accountId) {
        transactionService.applyInterestOnOverdraft(accountId);
        return new ResponseEntity<>("Interest applied on overdraft successfully.", HttpStatus.OK);
    }
}
