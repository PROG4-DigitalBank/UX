package com.cosmetica.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long transactionId;
    private Long accountId;
    private BigDecimal amount;
    private String transactionType;
    private Timestamp transactionDate;
    private String transactionReason;
}
