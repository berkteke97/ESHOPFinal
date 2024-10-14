package com.example.teke.ESHOP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Category {

    @Id
    private UUID id;
    private String name;

}
