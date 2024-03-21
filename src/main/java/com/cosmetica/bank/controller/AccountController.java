package com.cosmetica.bank.controller;

import com.cosmetica.bank.model.Account;
import com.cosmetica.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/update")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(account);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountId}/enable-overdraft")
    public ResponseEntity<Void> enableOverdraft(@PathVariable Long accountId) {
        accountService.enableOverdraft(accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}/disable-overdraft")
    public ResponseEntity<Void> disableOverdraft(@PathVariable Long accountId) {
        accountService.disableOverdraft(accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/calculate-allowed-credit")
    public ResponseEntity<BigDecimal> calculateAllowedCredit(@PathVariable Long accountId) {
        BigDecimal allowedCredit = accountService.calculateAllowedCredit(accountId);
        return ResponseEntity.ok(allowedCredit);
    }

    @PutMapping("/{accountId}/update-overdraft-interest-rates")
    public ResponseEntity<Void> updateOverdraftInterestRates(@PathVariable Long accountId,
            @RequestParam BigDecimal interestRateFirstSevenDays,
            @RequestParam BigDecimal interestRateAfterSevenDays) {
        accountService.updateOverdraftInterestRates(accountId, interestRateFirstSevenDays, interestRateAfterSevenDays);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/current-balance")
    public ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable Long accountId) {
        BigDecimal balance = accountService.getCurrentBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{accountId}/is-overdraft-enabled")
    public ResponseEntity<Boolean> isOverdraftEnabled(@PathVariable Long accountId) {
        boolean overdraftEnabled = accountService.isOverdraftEnabled(accountId);
        return ResponseEntity.ok(overdraftEnabled);
    }
}
