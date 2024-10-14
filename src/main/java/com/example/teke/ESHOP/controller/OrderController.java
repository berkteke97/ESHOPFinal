package com.example.teke.ESHOP.controller;

import com.example.teke.ESHOP.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
//
//    @PostMapping("/addToBasket")
//    public Boolean addToBasket(@RequestParam("username") String username, @RequestParam("barcode") String barcode , @RequestParam("productCount") Integer productCount) throws Exception {
//        return orderService.addToBasket(username, barcode, productCount);
//    }


}