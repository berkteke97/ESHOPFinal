package com.example.teke.ESHOP.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;


@JsonPropertyOrder({
        "id",
        "username",
        "password",
        "name",
        "surname",
        "email",
        "phone",
        "address",
        "agreement",
        "activeOrder"
})
@Data
@Entity
public class Customer {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private Boolean agreement;
    private Boolean activeOrder = false;
    @Enumerated(EnumType.STRING)
    private Role role ;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<UserInteraction> interactions;

}
