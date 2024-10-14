package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.dto.CustomerDTO;
import com.example.teke.ESHOP.exceptions.CustomerNotFoundException;
import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Role;
import com.example.teke.ESHOP.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service

public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Customer addCustomer(CustomerDTO customerDTO) throws Exception {
        Optional<Customer> existCustomer = customerRepository.findByUsername(customerDTO.getUsername());

        if (Objects.nonNull(existCustomer)){
            throw new RuntimeException("Customer exist");
        }

        Customer newCustomer = new Customer();
        newCustomer.setId(UUID.randomUUID());
        newCustomer.setUsername(customerDTO.getUsername());
        newCustomer.setPassword(customerDTO.getPassword());
        newCustomer.setName(customerDTO.getName());
        newCustomer.setSurname(customerDTO.getSurname());
        newCustomer.setEmail(customerDTO.getEmail());
        newCustomer.setPhone(customerDTO.getPhone());
        newCustomer.setAddress(customerDTO.getAddress());
        newCustomer.setRole(Role.valueOf(customerDTO.getUser_role()));

        return customerRepository.save(newCustomer);
    }

    public Optional<Customer> getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public Customer updateCustomer(CustomerDTO customerDTO) throws Exception{

        Customer existCustomer = customerRepository.findByUsername(customerDTO.getUsername()).orElseThrow(
                () -> new CustomerNotFoundException("Customer not exist")
        );


        if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()){
            existCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        }


        existCustomer.setName(customerDTO.getName());
        existCustomer.setUsername(customerDTO.getUsername());
        existCustomer.setAddress(customerDTO.getAddress());
        existCustomer.setEmail(customerDTO.getEmail());
        existCustomer.setPhone(customerDTO.getPhone());

        return customerRepository.save(existCustomer);
    }

    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(
                ()-> new CustomerNotFoundException("Customer not exist")
        );
    }

/*
    public Boolean login(String username, String password) {

        SecurityConfig securityConfig = new SecurityConfig();


        Customer existCustomer = customerRepository.findByUsername(username);
        if (existCustomer.getUsername().equals(username) && existCustomer.getPassword().equals(password)) {

            return true;
            //securityConfig.userDetailsService().loadUserByUsername().getPassword();
        }
        return false;
    }


*/
}
