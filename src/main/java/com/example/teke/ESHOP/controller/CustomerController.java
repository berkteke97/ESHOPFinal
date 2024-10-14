package com.example.teke.ESHOP.controller;

import com.example.teke.ESHOP.dto.CustomerDTO;
import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;


@CrossOrigin
@RestController
@RequestMapping("/account")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    CustomerService customerService;

    @GetMapping("/current")
    public ResponseEntity<Optional<Customer>> getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            log.info("username"+userDetails.getUsername());

            return ResponseEntity.ok(customerService.getCustomerByUsername(userDetails.getUsername()));
        } else {
            log.warn("No authenticated user found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/updateCustomer")
    public Customer updateCustomer(@RequestBody CustomerDTO customerDTO) throws Exception {
        return customerService.updateCustomer(customerDTO);
    }

    @GetMapping("/customers")
    public Iterable<Customer> findAll(){
        return customerService.findAll();
    }


}

