package com.example.teke.ESHOP.controller;

import com.example.teke.ESHOP.auth.AuthService;
import com.example.teke.ESHOP.model.*;
import com.example.teke.ESHOP.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    private ResponseEntity<RegistrationResponse> registerCustomer(
            @RequestBody RegistrationRequest registrationRequest
    ) {

        return ResponseEntity.ok(authService.register(registrationRequest).getBody());
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponse> authenticateCustomer(
            @RequestBody LoginRequest loginRequest
    ){
        return ResponseEntity.ok(authService.signin(loginRequest).getBody());
    }

    @PostMapping("/logout")
    private ResponseEntity<?> logoutUser(HttpServletRequest request){

        return ResponseEntity.ok().body(authService.logoutUser(request));
    }


//    @GetMapping("/getUserInfo")
//    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String token) {
//        Claims claims = decodeToken(token.replace("Bearer ", ""));
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("userId", claims.get("userId"));
//        userInfo.put("username", claims.get("username"));
//        userInfo.put("email", claims.get("email"));
//
//        return ResponseEntity.ok(userInfo);
//    }
}
