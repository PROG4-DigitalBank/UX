// CustomerController.java
package com.cosmetica.bank.controller;

import com.cosmetica.bank.model.Customer;
import com.cosmetica.bank.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Customer> signUp(@RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password,
                                           @RequestParam String phoneNumber) {
        Customer customer = customerService.signUp(firstName, lastName, email, password, phoneNumber);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestParam String email, @RequestParam String password) {
        try {
            Customer customer = customerService.login(email, password);
            return ResponseEntity.ok(customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
