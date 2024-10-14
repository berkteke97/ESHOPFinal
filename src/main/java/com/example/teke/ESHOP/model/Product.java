package com.example.teke.ESHOP.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data
@Entity
public class Product {

    @Id
    private UUID id;
    private BigDecimal price;
    private String categoryName;            //Category classındaki name ile eşdeğer ise
    private String brand;
    private int stock;
    private byte[] imageUrl;
    private String detail;
    private String barcode;
    //private int counter;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<UserInteraction> interactions;


    public Product(){}

    public Product(String barcode, String brand, String categoryName,String detail){
        this.barcode = barcode;
        this.brand = brand;
        this.categoryName = categoryName;
        this.detail = detail;
    }



}
