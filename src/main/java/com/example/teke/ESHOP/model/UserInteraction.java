package com.example.teke.ESHOP.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JsonIgnoreProperties("interactions")
    private Customer customer;

    @ManyToOne
    @JsonBackReference
    @JsonIgnoreProperties("interactions")
    private Product product;
    private LocalDateTime viewedAt;

    public UserInteraction(Customer customer, Product product, LocalDateTime now) {
        this.customer = customer;
        this.product = product;
        this.viewedAt = now;
    }
}
