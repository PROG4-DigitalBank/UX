package com.cosmetica.bank.service;

public interface CustomerService {
    Customer signUp(String firstName, String lastName, String email, String password, String phoneNumber);
    Customer login(String email, String password);
}
