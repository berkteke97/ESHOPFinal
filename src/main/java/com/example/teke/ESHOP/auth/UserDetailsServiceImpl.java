package com.example.teke.ESHOP.auth;

import com.example.teke.ESHOP.exceptions.CustomerNotFoundException;
import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer=
                customerRepository.findByUsername(username).orElseThrow(
                        ()-> new CustomerNotFoundException("Customer Not Found")
                );

        List<SimpleGrantedAuthority> authorities = customer.getRole().getAuthorities();
        System.out.println("authorities:"+authorities);
        return  new org.springframework.security.core.userdetails.User(
                customer.getUsername(),
                customer.getPassword(),
                authorities
        );

    }
}
