package com.example.teke.ESHOP.repository;

import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInteractionRepository  extends JpaRepository<UserInteraction,Long> {
    List<UserInteraction> findByProduct(Product product);
    List<UserInteraction> findByCustomer(Customer customer);

    Optional<UserInteraction> findByCustomerAndProduct(Customer customer, Product product);
}
