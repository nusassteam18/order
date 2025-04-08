package com.ss.order.controller;

import com.ss.order.helperDTO.OrderDTO;
import com.ss.order.requestDTO.SendOrderRequest;
import com.ss.order.requestDTO.SendOrderStatusUpdateRequest;
import com.ss.order.responseDTO.ReportResponseDTO;
import com.ss.order.responseDTO.SendOrderStatusUpdateResponse;
import com.ss.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/report/{restaurantId}")
    public ResponseEntity<ReportResponseDTO> getPastThreeMonthsRevenueReport(@PathVariable Long restaurantId) {
        log.info("restaurantId: {}", restaurantId);
        return ResponseEntity.ok(orderService.getPastThreeMonthsRevenueReport(restaurantId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable Long restaurantId) {
        log.info("restaurantId: {}", restaurantId);
        return ResponseEntity.ok(orderService.getOrders(restaurantId));
    }

    @PutMapping
    public ResponseEntity<SendOrderStatusUpdateResponse> updateOrderStatus(@RequestBody SendOrderStatusUpdateRequest sendOrderStatusUpdateRequest) {
        SendOrderStatusUpdateResponse response = orderService.processOrderUpdate(sendOrderStatusUpdateRequest);

        if (response.getReturnCode() < -1)
            return ResponseEntity.unprocessableEntity().build();

        log.info("response: {}", response);
        return ResponseEntity.ok(response);
    }
}