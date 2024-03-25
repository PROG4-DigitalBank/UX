package com.cosmetica.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String accountNumber;
    private String firstName;
    private String lastName;
    private BigDecimal balance;
    private BigDecimal monthlySalary;
    private Date dateOfBirth;
    private boolean allowsOverdraft;
    private BigDecimal overdraftLimit;
    private BigDecimal overdraftInterestRate;
    private BigDecimal loanInterest;
    private LocalDateTime createdAt;
    private String bankName;

}
