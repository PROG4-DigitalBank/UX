package com.cosmetica.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long transactionId;
    private String accountNumber;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime transactionDate;
    private String transactionReason;
    private LocalDateTime effectiveDateTime;
    private String transactionStatus;
}
