package com.cosmetica.bank.repository;

import org.springframework.data.annotation.Id;

import java.util.List;

public interface CrudInterface<T, ID> {
    // Create
    T save(T entity);

    List<T> findAll();

    // Update
    T update(T entity);

    // Delete
    void delete(T entity);
    void deleteById(ID id);
}
