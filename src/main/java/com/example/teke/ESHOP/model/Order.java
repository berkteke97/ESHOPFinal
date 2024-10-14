package com.example.teke.ESHOP.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data
@Document
public class Order {
    @Id
    private UUID id;
    private String username;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountAfterDiscount;
    private BigDecimal campaignDiscount;
    private BigDecimal deliveryCost;
    private List<String> barcode; //Alınacak ürünlerin barkod numarasınu ekler
    private List<Integer> productCount; //barcode list'i ile aynı sıralamada ürünlerin sayılarını eklers

}
