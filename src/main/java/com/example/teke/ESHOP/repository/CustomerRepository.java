package com.example.teke.ESHOP.repository;

import com.example.teke.ESHOP.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmail(String email);
}