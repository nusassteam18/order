package com.ss.order.service;

import com.ss.order.entity.Order;
import com.ss.order.repository.OrderRepository;
import com.ss.order.requestDTO.SendOrderRequest;
import com.ss.order.responseDTO.ReportResponseDTO;
import com.ss.order.responseDTO.RevenueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class OrderService {
    Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Long createOrder(SendOrderRequest sendOrderRequest) {
        log.info("sendOrderRequest: {}", sendOrderRequest);
        Order order = new Order(null, sendOrderRequest.getCustomerId(), sendOrderRequest.getRestaurantId(), Order.Status.Pending, sendOrderRequest.getOrderItems());

        return orderRepository.save(order).getOrderId();
    }

    public ReportResponseDTO getPastThreeMonthsRevenueReport(Long restaurantId) {
        // Get current date as LocalDate
        LocalDate now = LocalDate.now();
        List<RevenueDTO> revenues = new ArrayList<>();

        // Retrieve orders for the given restaurant
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);

        // Loop for past three months (current month inclusive)
        // For example, if today is April 2025, we need Feb, March, April
        // Here, i=2 gives 2 months ago, i=1 gives last month, i=0 gives current month.
        for (int i = 2; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            // Start of the month
            LocalDate startOfMonth = monthDate.withDayOfMonth(1);
            // End of the month (exclusive)
            LocalDate endOfMonth = startOfMonth.plusMonths(1);

            // Filter orders whose createdAt falls within [startOfMonth, endOfMonth)
            List<Order> filteredOrders = orders.stream()
                    .filter(order -> {
                        LocalDate createdAt = order.getCreatedAt().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return (!createdAt.isBefore(startOfMonth) && createdAt.isBefore(endOfMonth));
                    })
                    .toList();

            // Calculate revenue for the month
            double monthRevenue = filteredOrders.stream()
                    .mapToDouble(order -> order.getOrderItems().stream()
                            .mapToDouble(item ->
                                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                                            .doubleValue()
                            ).sum())
                    .sum();

            // Count the number of orders
            int orderCount = filteredOrders.size();

            // Convert the start of the month (LocalDate) to java.util.Date.
            // Using java.sql.Date.valueOf provides a Date with the correct value.
            Date firstDayOfMonth = java.sql.Date.valueOf(startOfMonth);

            // Create a new RevenueDTO for this month.
            revenues.add(new RevenueDTO(firstDayOfMonth, monthRevenue, orderCount));
        }

        return new ReportResponseDTO(restaurantId, revenues);
    }

}
