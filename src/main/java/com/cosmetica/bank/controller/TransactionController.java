package com.cosmetica.bank.controller;

import com.cosmetica.bank.model.Transaction;
import com.cosmetica.bank.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Map<String, String> requestBody) {
        String accountNumber = requestBody.get("accountNumber");
        BigDecimal amount = new BigDecimal(requestBody.get("amount"));
        String message = transactionService.deposit(accountNumber, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Map<String, String> requestBody) {
        String accountNumber = requestBody.get("accountNumber");
        BigDecimal amount = new BigDecimal(requestBody.get("amount"));
        String message = transactionService.withdraw(accountNumber, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody Map<String, String> requestBody) {
        String sourceAccountNumber = requestBody.get("sourceAccountNumber");
        String targetAccountNumber = requestBody.get("targetAccountNumber");
        BigDecimal amount = new BigDecimal(requestBody.get("amount"));
        String message = transactionService.transfer(sourceAccountNumber, targetAccountNumber, amount);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/schedule-transfer")
    public ResponseEntity<String> scheduleTransfer(@RequestBody Map<String, Object> requestBody) {
        String sourceAccountNumber = (String) requestBody.get("sourceAccountNumber");
        String targetAccountNumber = (String) requestBody.get("targetAccountNumber");
        BigDecimal amount = new BigDecimal((String) requestBody.get("amount"));
        LocalDateTime effectiveDateTime = LocalDateTime.parse((CharSequence) requestBody.get("effectiveDateTime"));
        String message = transactionService.scheduleTransfer(sourceAccountNumber, targetAccountNumber, amount,
                effectiveDateTime);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/cancel-scheduled-transfer")
    public ResponseEntity<String> cancelScheduledTransfer(@RequestBody Map<String, String> requestBody) {
        Long transactionId = Long.parseLong(requestBody.get("transactionId"));
        String message = transactionService.cancelScheduledTransfer(transactionId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
