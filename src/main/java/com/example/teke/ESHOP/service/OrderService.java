package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Order;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.repository.CustomerRepository;
import com.example.teke.ESHOP.repository.OrderRepository;
import com.example.teke.ESHOP.repository.ProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    ProductRepository productRepository;
//
//    public Boolean addToBasket(String username, String barcode, Integer productCount) {
//
//        Customer existCustomer = customerRepository.findByUsername(username);
//
//        Boolean activeOrder = existCustomer.getActiveOrder();
//
//        if (!activeOrder) {
//            createOrder(username);
//            existCustomer.setActiveOrder(true);
//            customerRepository.save(existCustomer);
//            addItem(username, barcode, productCount);
//        } else {
//            addItem(username, barcode, productCount);
//        }
//
//
//        return true;
//    }

    public Order createOrder(String username) {

        Order order = new Order();

        order.setId(UUID.randomUUID());
        order.setUsername(username);
        order.setTotalAmount(new BigDecimal(0));
        order.setTotalAmountAfterDiscount(new BigDecimal(0));
        order.setCampaignDiscount(new BigDecimal(0));
        order.setDeliveryCost(new BigDecimal(0));
        order.setBarcode(Collections.emptyList());
        order.setProductCount(Collections.emptyList());

        Order newOrder = mongoTemplate.save(order);
        return newOrder;
    }

    public Order addItem(String username, String barcode, int productCount) {

        Order existOrder = orderRepository.findByUsername(username);
        existOrder.getBarcode().add(barcode);
        existOrder.getProductCount().add(productCount);
        BigDecimal productPrice = productRepository.findByBarcode(barcode).getPrice();
        BigDecimal actualAmount = existOrder.getTotalAmount();
        existOrder.setTotalAmount((productPrice.multiply(BigDecimal.valueOf(productCount))).add(actualAmount));
        return orderRepository.save(existOrder);
    }
}