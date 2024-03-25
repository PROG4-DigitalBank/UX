package com.cosmetica.bank.repository;

import java.util.List;

public interface CrudInterface<T, ID> {
    // Create
    T save(T entity);

    // Read
    T findByAccountNumber(String accountNumber);

    List<T> findAll();

    // Update
    T update(T entity);

    // Delete
    void delete(T entity);
    void deleteById(ID id);
}
