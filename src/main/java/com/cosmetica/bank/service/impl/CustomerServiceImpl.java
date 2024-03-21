package com.cosmetica.bank.service.impl;

import com.cosmetica.bank.component.JwtTokenProvider;
import com.cosmetica.bank.model.Customer;
import com.cosmetica.bank.repository.CustomerRepository;
import com.cosmetica.bank.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public CustomerServiceImpl(CustomerRepository customerRepository, JwtTokenProvider jwtTokenProvider) {
        this.customerRepository = customerRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Customer signUp(String firstName, String lastName, String email, String password, String phoneNumber) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhoneNumber(phoneNumber);

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

    public Long getCurrentCustomerId(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            return customer.getCustomerId();
        } else {
            throw new IllegalArgumentException("Customer not found for token");
        }
    }
}
