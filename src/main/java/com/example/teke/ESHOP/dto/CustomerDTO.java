package com.example.teke.ESHOP.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private String username;		//  username
    private String password;		//  Password
    private String name;			//  Name
    private String surname;			//  surname
    private String email;		    //  Email
    private String phone;		    //  phone number
    private String address;         //  Address
    private Boolean agreement;
    private Boolean activeOrder;
    private String user_role;
}
