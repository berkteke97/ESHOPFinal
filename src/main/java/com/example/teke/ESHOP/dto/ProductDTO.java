package com.example.teke.ESHOP.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private BigDecimal price;
    private String categoryName;            //Category classındaki name ile eşdeğer ise
    private String brand;
   // private  byte[] imageUrl;
    private int stock;
    private String detail;
    private String barcode;
    //private int counter;


    private MultipartFile imageFile;


}
