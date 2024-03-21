package com.cosmetica.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long accountId;
    private Long customerId;
    private String accountNumber;
    private BigDecimal balance;
    private BigDecimal monthlySalary;
    private boolean allowsOverdraft;
    private BigDecimal overdraftLimit;
    private BigDecimal overdraftInterestRate;
    private BigDecimal loanInterest;
    private LocalDateTime createdAt;
    private String bankName;
}
