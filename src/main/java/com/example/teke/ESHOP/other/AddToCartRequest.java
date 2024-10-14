package com.example.teke.ESHOP.other;

import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;

import java.util.UUID;

public record AddToCartRequest(
        UUID productId,
        Integer quantity
) {
}
