package com.ss.order.controller;

import com.ss.order.requestDTO.SendOrderRequest;
import com.ss.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/orders")
public class OrderController {

    Logger log =  LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody SendOrderRequest sendOrderRequest) {
        Long orderId = orderService.createOrder(sendOrderRequest);
        return ResponseEntity.ok(orderId);
    }
}