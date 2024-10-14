package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.model.UserInteraction;
import com.example.teke.ESHOP.repository.UserInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollaborativeRecommendationService {

    private final UserInteractionRepository userInteractionRepository;

    public List<Product> getCollaborativeRecommendation(Product currentProduct, Customer currentCustomer) {

        List<UserInteraction> interactions = userInteractionRepository.findByProduct(currentProduct);
        Set<Product> recommendedProducts = new HashSet<>();

        for (UserInteraction interaction : interactions) {
            Customer otherUser = interaction.getCustomer();

            if (!otherUser.equals(currentCustomer)) {
                List<UserInteraction> otherUserInteractions = userInteractionRepository.findByCustomer(otherUser);
                for (UserInteraction otherUserInteraction : otherUserInteractions) {
                    if (!otherUserInteraction.getProduct().equals(currentProduct)) {
                        recommendedProducts.add(otherUserInteraction.getProduct());
                    }
                }
            }
        }
        return new ArrayList<>(recommendedProducts);
    }


}
