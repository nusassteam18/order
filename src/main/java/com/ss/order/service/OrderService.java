package com.ss.order.service;

import com.ss.order.entity.Order;
import com.ss.order.repository.OrderRepository;
import com.ss.order.requestDTO.SendOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {
    Logger log =  LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Long createOrder(SendOrderRequest sendOrderRequest){
        log.info("sendOrderRequest: {}", sendOrderRequest);
        Order order = new Order(null, sendOrderRequest.getCustomerId(), sendOrderRequest.getRestaurantId(), Order.Status.Pending, sendOrderRequest.getOrderItems());

        return orderRepository.save(order).getOrderId();
    }

}
