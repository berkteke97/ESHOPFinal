package com.example.teke.ESHOP.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private String username;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountAfterDiscount;
    private BigDecimal campaignDiscount;
    private BigDecimal deliveryCost;
    private List<String> barcode; //Alınacak ürünleri ekler
    private List<Integer> productCount; //barcode list'i ile aynı sıralamada olacak. Barcode kısmında eklenen ürünlerden kaç tane eklendiği burada tutulacak
}
