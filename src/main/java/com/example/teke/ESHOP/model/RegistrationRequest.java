package com.example.teke.ESHOP.model;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private Boolean agreement;
    private Role user_role;
}
