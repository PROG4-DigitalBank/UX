package com.cosmetica.bank.controller;

import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        String message = transactionService.deposit(accountNumber, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        String message = transactionService.withdraw(accountNumber, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam String sourceAccountNumber, @RequestParam String targetAccountNumber,
                                           @RequestParam BigDecimal amount) {
        String message = transactionService.transfer(sourceAccountNumber, targetAccountNumber, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/schedule-transfer")
    public ResponseEntity<String> scheduleTransfer(@RequestParam String sourceAccountNumber, @RequestParam String targetAccountNumber,
                                                   @RequestParam BigDecimal amount, @RequestParam LocalDateTime effectiveDateTime) {
        String message = transactionService.scheduleTransfer(sourceAccountNumber, targetAccountNumber, amount, effectiveDateTime);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/cancel-scheduled-transfer")
    public ResponseEntity<String> cancelScheduledTransfer(@RequestParam Long transactionId) {
        String message = transactionService.cancelScheduledTransfer(transactionId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
