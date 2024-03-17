package com.cosmetica.bank.service.impl;

import java.math.BigDecimal;

import com.cosmetica.bank.model.Customer;
import com.cosmetica.bank.repository.CustomerRepository;
import com.cosmetica.bank.service.CustomerService;

public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer signUp(String firstName, String lastName, String email, String password, String phoneNumber, BigDecimal monthlySalary) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhoneNumber(phoneNumber);
        customer.setMonthlySalary(monthlySalary);
        
        return customerRepository.save(customer);
    }
    @Override
    public Customer login(String email, String password) {
        Customer customer = customerRepository.findByEmail(email);
        
        if (customer != null) {
            if (customer.getPassword().equals(password)) {
                return customer; 
            } else {
                throw new IllegalArgumentException("Incorrect password"); 
            }
        } else {
            throw new IllegalArgumentException("Email not found"); 
        }
    }
}