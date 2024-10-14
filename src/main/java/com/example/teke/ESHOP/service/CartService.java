package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.dto.ProductDTO;
import com.example.teke.ESHOP.exceptions.InsufficientStockException;
import com.example.teke.ESHOP.exceptions.ProductNotFoundException;
import com.example.teke.ESHOP.model.Cart;
import com.example.teke.ESHOP.model.CartItem;
import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.other.AddToCartRequest;
import com.example.teke.ESHOP.repository.CartItemRepository;
import com.example.teke.ESHOP.repository.CartRepository;
import com.example.teke.ESHOP.repository.CustomerRepository;
import com.example.teke.ESHOP.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Cart getOrCreateCart(Customer customer) {
        return cartRepository.findByCustomerId(customer.getId()).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setCustomer(customer);
            return cartRepository.save(cart);
        });
    }

    public Cart addProductToCart(Customer customer, AddToCartRequest addToCartRequest) throws InsufficientStockException {
        Cart cart = getOrCreateCart(customer);
        Product product = productRepository.findById(addToCartRequest.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(addToCartRequest.productId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.quantity());
        } else {
            // Create a new cart item if it doesn't exist in the cart
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(addToCartRequest.quantity());
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }


        if (addToCartRequest.quantity() > product.getStock()) {
            throw new InsufficientStockException("Not enough stock available for product");
        }


        product.setStock(product.getStock() - addToCartRequest.quantity());
        productRepository.save(product);

        cartItemRepository.save(cartItem);
        return cartRepository.save(cart);
    }


    public Cart removeItemFromCart(Customer customer, Long productId) {
        Cart cart = getOrCreateCart(customer);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);


        return cartRepository.save(cart);
    }
}
