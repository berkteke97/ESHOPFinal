package com.example.teke.ESHOP.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private UUID productId;
    private int quantity;
    private BigDecimal price;
    private String detail;

    private byte[] imageFile;
}
