package com.example.teke.ESHOP.repository;

import com.example.teke.ESHOP.model.Cart;
import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomer(Customer customer);
    Optional<Cart> findByCustomerId(UUID customer_id);
}
