package com.example.teke.ESHOP.auth;

import com.example.teke.ESHOP.exceptions.CustomerExistsException;
import com.example.teke.ESHOP.model.*;
import com.example.teke.ESHOP.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;


    /// asıl registerı burada yapıyoruz. eğer burada istenen şekilde auth işlemleri olmazsa customer servicede olan add customer metodunu da kullabiliriz ilerleyen zamanlarda?!
    public ResponseEntity<RegistrationResponse> register(RegistrationRequest registrationRequest) {

        boolean usernameExists= customerRepository.findByUsername(registrationRequest.getUsername()).isPresent();
        if(usernameExists){
            throw  new CustomerExistsException("Customer already exists, please try logging in");
        }
        boolean emailExists= customerRepository.findByEmail(registrationRequest.getEmail()).isPresent();
        if(emailExists){
            throw  new CustomerExistsException("Customer already exists, please try logging in");
        }
        String userRole = registrationRequest.getUser_role() != null ? String.valueOf(registrationRequest.getUser_role()) : Role.MEMBER.name();


        var customer = new Customer();
        customer.setUsername(registrationRequest.getUsername());
        customer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        customer.setEmail(registrationRequest.getEmail());
        customer.setName(registrationRequest.getName());
        customer.setSurname(registrationRequest.getSurname());
        customer.setPhone(registrationRequest.getPhone());
        customer.setRole(Role.valueOf(userRole));
        customer.setAgreement(registrationRequest.getAgreement());
         customer.setAddress(registrationRequest.getAddress());

//        Prevent registering more than 1 customer with the same username or email



        customerRepository.save(customer);
        UserDetails userDetails = userDetailsService.loadUserByUsername(registrationRequest.getUsername());



        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, registrationRequest.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email",customer.getEmail());
        claims.put("role",customer.getRole().name());
        String token = jwtService.generateToken(claims,userDetails);
        //        Setting  up auth cookies
        ResponseCookie authCookie = ResponseCookie.from("token", token).maxAge(6000).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, authCookie.toString());
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setToken(token);
        registrationResponse.setUser(registrationRequest.getSurname());

        return ResponseEntity.ok().headers(headers).body(registrationResponse);


    }

    public ResponseEntity<LoginResponse> signin(LoginRequest loginRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUser());



        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, loginRequest.getPass(),
                userDetails.getAuthorities());
//        Current Authentication status
        System.out.println(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUser(),loginRequest.getPass())
        );

        Customer customer= customerRepository.findByUsername(loginRequest.getUser())
                .orElseThrow(()-> new UsernameNotFoundException("Customer not found,please try again"));
        Map<String, Object> claims = new HashMap<>();
        claims.put("email",customer.getEmail());
        claims.put("role",customer.getRole().name());

        String token= jwtService.generateToken(claims,userDetails);

//        Setting  up auth cookies
        ResponseCookie authCookie=ResponseCookie.from("token",token).maxAge(6000).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,authCookie.toString());

        LoginResponse loginResponse= new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUser(loginRequest.getUser());


        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        try {


            SecurityContextHolder.clearContext();
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                jwtService.invalidateToken(token);
            }

        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return ResponseEntity.ok().body("Logged out successfully");
    }
}
