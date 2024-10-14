package com.example.teke.ESHOP.repository;

import com.example.teke.ESHOP.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface OrderRepository extends MongoRepository<Order, UUID> {

    Order findByUsername(String username);

}
