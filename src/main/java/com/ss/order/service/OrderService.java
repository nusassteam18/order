package com.ss.order.service;

import com.ss.order.constant.Constants;
import com.ss.order.entity.Order;
import com.ss.order.entity.OrderItem;
import com.ss.order.helperDTO.CustomerTruncatedDTO;
import com.ss.order.helperDTO.OrderDTO;
import com.ss.order.helperDTO.OrderItemDTO;
import com.ss.order.helperDTO.ProductTruncatedDTO;
import com.ss.order.repository.OrderRepository;
import com.ss.order.requestDTO.SendOrderRequest;
import com.ss.order.requestDTO.SendOrderStatusUpdateRequest;
import com.ss.order.responseDTO.ReportResponseDTO;
import com.ss.order.responseDTO.RevenueDTO;
import com.ss.order.responseDTO.SendOrderStatusUpdateResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OrderService {
    Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    @Autowired
    public WebClient.Builder webClientBuilder;

    @Value("${users.url:http://127.0.0.1:8081/users}")
    private String USERS_URL;

    @Value("${restaurants.url:http://127.0.0.1:8082/restaurants}")
    private String RESTAURANTS_URL;

    public Long createOrder(SendOrderRequest sendOrderRequest) {
        log.info("sendOrderRequest: {}", sendOrderRequest);
        Order order = new Order(null, sendOrderRequest.getCustomerId(), sendOrderRequest.getRestaurantId(), Order.Status.Pending, sendOrderRequest.getOrderItems());

        return orderRepository.save(order).getOrderId();
    }

    public List<OrderDTO> getOrders(Long restaurantId) {
        List<Order> orders = orderRepository.findByRestaurantIdOrderByOrderIdDesc(restaurantId);

        List<CustomerTruncatedDTO> customerTruncatedDTOS = webClientBuilder.build()
                .get()
                .uri(USERS_URL + "/customers/truncated")
                .retrieve()
                .bodyToFlux(CustomerTruncatedDTO.class)
                .collectList()
                .block();

        List<ProductTruncatedDTO> productTruncatedDTOS = webClientBuilder.build()
                        .get()
                        .uri(RESTAURANTS_URL + "/products/truncated/" + restaurantId)
                        .retrieve()
                        .bodyToFlux(ProductTruncatedDTO.class)
                        .collectList()
                        .block();

        if (orders == null)
            return null;

        ArrayList<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            // Set details from Order entity
            orderDTO.setId(order.getOrderId());
            orderDTO.setCreatedAt(order.getCreatedAt());
            orderDTO.setUpdatedAt(order.getUpdatedAt());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setTotal(order.getTotal());

            // Set details from CustomerTruncatedDTO
            if (customerTruncatedDTOS != null) {
                CustomerTruncatedDTO customer = customerTruncatedDTOS.stream().filter(customerTruncatedDTO ->
                        Objects.equals(customerTruncatedDTO.getUserId(), order.getCustomerId()))
                        .collect(toSingleton());

                if (customer != null) {
                    orderDTO.setFirstName(customer.getFirstName());
                    orderDTO.setLastName(customer.getLastName());
                    orderDTO.setContactNo(customer.getContactNo());
                    orderDTO.setEmail(customer.getEmail());
                }
            }

            List<OrderItem> orderItems = order.getOrderItems();
            if (orderItems != null) {
                ArrayList<OrderItemDTO> orderItemDTOs = new ArrayList<>();
               for (OrderItem orderItem : orderItems) {
                   OrderItemDTO orderItemDTO = new OrderItemDTO();
                   // Set details from OrderItem entity
                   orderItemDTO.setId(orderItem.getOrderItemId());
                   orderItemDTO.setQuantity(orderItem.getQuantity());
                   orderItemDTO.setSpecialInstructions(orderItem.getDescription());
                   orderItemDTO.setSubtotal(orderItem.getPrice());

                   if (productTruncatedDTOS != null) {
                       ProductTruncatedDTO product = productTruncatedDTOS.stream().filter(productTruncatedDTO ->
                               Objects.equals(productTruncatedDTO.getProductId(), orderItem.getProductId()))
                               .collect(toSingleton());

                       if (product != null)
                        orderItemDTO.setDish(product.getName());
                   }
                   orderItemDTOs.add(orderItemDTO);
               }
               orderDTO.setOrderItemDTOs(orderItemDTOs);
            }
            orderDTOs.add(orderDTO);
        }

//        log.info("orders: {}", orders);
//        log.info("orderDTOs: {}", orderDTOs);
//        log.info("customerTruncatedDTOS: {}", customerTruncatedDTOS);
//        log.info("productTruncatedDTOS: {}", productTruncatedDTOS);

//        return restTemplate.getForObject(USERS_URL, UserResponseDTO.class);
         return orderDTOs;
    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        return null;
                    }
                    return list.get(0);
                }
        );
    }


    public SendOrderStatusUpdateResponse processOrderUpdate(@RequestBody SendOrderStatusUpdateRequest sendOrderStatusUpdateRequest) {
        final int returnCode = updateOrderStatus(sendOrderStatusUpdateRequest);

        if (returnCode == Constants.ORDER_NOT_FOUND || returnCode == Constants.SAME_ORDER_STATUS)
            return new SendOrderStatusUpdateResponse(returnCode);

        Order orderUpdated = orderRepository.findById(sendOrderStatusUpdateRequest.getOrderId()).orElse(null);

        if (orderUpdated == null)
            return new SendOrderStatusUpdateResponse(Constants.ORDER_STATUS_UPDATE_FAILED);

        return new SendOrderStatusUpdateResponse(Constants.SUCCESS, orderUpdated.getOrderId(), orderUpdated.getStatus(), orderUpdated.getUpdatedAt());
    }

    @Transactional
    private int updateOrderStatus(SendOrderStatusUpdateRequest sendOrderStatusUpdateRequest) {
        Order order = orderRepository.findById(sendOrderStatusUpdateRequest.getOrderId()).orElse(null);
        if (order == null) return Constants.ORDER_NOT_FOUND;

        if (order.getStatus() == sendOrderStatusUpdateRequest.getStatus())
            return Constants.SAME_ORDER_STATUS; // no need to update status

        order.setStatus(sendOrderStatusUpdateRequest.getStatus());
        order.setUpdatedAt(new Date());
        orderRepository.save(order);

        return Constants.SUCCESS;
    }

    private int sendEmail() {
        String email = "sscustomer123@gmail.com";
        try {
            emailService.test(email);
        } catch (Exception e) {
            log.error("Error sending email to {}", email);
            log.error(String.valueOf(e));
            return Constants.SUCCESS_BUT_SEND_EMAIL_FAILED;
        }
        log.info("Send email to {} success", email);
        return Constants.SUCCESS;
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
