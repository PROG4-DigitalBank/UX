package com.cosmetica.bank.service;

import java.math.BigDecimal;

import com.cosmetica.bank.model.Customer;

public interface CustomerService {
    Customer signUp(String firstName, String lastName, String email, String password, String phoneNumber, BigDecimal monthlySalary);
    Customer login(String email, String password);
}
