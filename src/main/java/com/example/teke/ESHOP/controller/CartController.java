package com.example.teke.ESHOP.controller;

import com.example.teke.ESHOP.exceptions.InsufficientStockException;
import com.example.teke.ESHOP.model.Cart;
import com.example.teke.ESHOP.model.CartItemResponse;
import com.example.teke.ESHOP.model.CartResponse;
import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.other.AddToCartRequest;
import com.example.teke.ESHOP.repository.CustomerRepository;
import com.example.teke.ESHOP.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CustomerRepository customerRepository;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestBody AddToCartRequest addToCartRequest) throws InsufficientStockException {
        Customer customer = getAuthenticatedCustomer();
        Cart updatedCart = cartService.addProductToCart(customer, addToCartRequest);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        Customer customer = getAuthenticatedCustomer();
        Cart cart = cartService.getOrCreateCart(customer);

        // Map Cart to CartResponse
        CartResponse cartResponse = mapToCartResponse(cart);

        return ResponseEntity.ok(cartResponse);
    }

    private Customer getAuthenticatedCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return customerRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.getId());

        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(item -> {
                    CartItemResponse itemResponse = new CartItemResponse();
                    itemResponse.setProductId(item.getProduct().getId());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setImageFile(item.getProduct().getImageUrl());
                    itemResponse.setDetail(item.getProduct().getDetail());
                    return itemResponse;
                })
                .collect(Collectors.toList());

        cartResponse.setItems(itemResponses);



        return cartResponse;
    }



}

