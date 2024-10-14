package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.model.UserInteraction;
import com.example.teke.ESHOP.repository.UserInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInteractionService {

    private final UserInteractionRepository userInteractionRepository;

    public void trackInteraction(Customer customer, Product product) {
        Optional<UserInteraction> existingInteraction = userInteractionRepository
                .findByCustomerAndProduct(customer, product);
        if (existingInteraction.isEmpty()){
            UserInteraction interaction = new UserInteraction(customer, product, LocalDateTime.now());
            userInteractionRepository.save(interaction);
        }

    }
}
