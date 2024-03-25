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

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
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

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountNumber}/enable-overdraft")
    public ResponseEntity<Void> enableOverdraft(@PathVariable String accountNumber) {
        accountService.enableOverdraft(accountNumber);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountNumber}/disable-overdraft")
    public ResponseEntity<Void> disableOverdraft(@PathVariable String accountNumber) {
        accountService.disableOverdraft(accountNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountNumber}/calculate-allowed-credit")
    public ResponseEntity<BigDecimal> calculateAllowedCredit(@PathVariable String accountNumber) {
        BigDecimal allowedCredit = accountService.calculateAllowedCredit(accountNumber);
        return ResponseEntity.ok(allowedCredit);
    }

    @PutMapping("/{accountNumber}/update-overdraft-interest-rates")
    public ResponseEntity<Void> updateOverdraftInterestRates(@PathVariable String accountNumber,
                                                             @RequestParam BigDecimal interestRateFirstSevenDays,
                                                             @RequestParam BigDecimal interestRateAfterSevenDays) {
        accountService.updateOverdraftInterestRates(accountNumber, interestRateFirstSevenDays, interestRateAfterSevenDays);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountNumber}/current-balance")
    public ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable String accountNumber) {
        BigDecimal balance = accountService.getCurrentBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{accountNumber}/is-overdraft-enabled")
    public ResponseEntity<Boolean> isOverdraftEnabled(@PathVariable String accountNumber) {
        boolean overdraftEnabled = accountService.isOverdraftEnabled(accountNumber);
        return ResponseEntity.ok(overdraftEnabled);
    }

    @GetMapping("/{accountNumber}/current-balance-with-loans-and-interest")
    public ResponseEntity<BigDecimal> getCurrentBalanceWithLoansAndInterest(@PathVariable String accountNumber) {
        BigDecimal balanceWithLoansAndInterest = accountService.getCurrentBalanceWithLoansAndInterest(accountNumber);
        return ResponseEntity.ok(balanceWithLoansAndInterest);
    }

}
