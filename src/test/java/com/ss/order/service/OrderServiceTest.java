package com.ss.order.service;

import com.ss.order.entity.Order;
import com.ss.order.helperDTO.CustomerTruncatedDTO;
import com.ss.order.helperDTO.ProductTruncatedDTO;
import com.ss.order.repository.OrderRepository;
import com.ss.order.requestDTO.SendOrderRequest;
import com.ss.order.requestDTO.SendOrderStatusUpdateRequest;
import com.ss.order.responseDTO.SendOrderResponse;
import com.ss.order.responseDTO.SendOrderStatusUpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, emailService);
        orderService.webClientBuilder = webClientBuilder;
    }

    @Test
    void testCreateOrder() {
        SendOrderRequest request = new SendOrderRequest();
        request.setCustomerId(1L);
        request.setRestaurantId(2L);
        request.setOrderItems(new ArrayList<>());

        Order savedOrder = new Order(100L, 1L, 2L, Order.Status.Pending, new ArrayList<>());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order order = orderService.saveOrder(request);
        Long orderId = order.getOrderId();

        assertEquals(100L, orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetOrders() {
        Long restaurantId = 5L;
        List<Order> mockOrders = List.of(new Order(1L, 10L, restaurantId, Order.Status.Pending, List.of()));
        when(orderRepository.findByRestaurantIdOrderByOrderIdDesc(restaurantId)).thenReturn(mockOrders);

        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(eq(CustomerTruncatedDTO.class))).thenReturn(Flux.fromIterable(List.of()));
        when(responseSpec.bodyToFlux(eq(ProductTruncatedDTO.class))).thenReturn(Flux.fromIterable(List.of()));

        var result = orderService.getOrders(restaurantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findByRestaurantIdOrderByOrderIdDesc(restaurantId);
    }

    @Test
    void testProcessOrderUpdate_success() {
        SendOrderStatusUpdateRequest request = new SendOrderStatusUpdateRequest();
        request.setOrderId(1L);
        request.setStatus(Order.Status.Completed);

        Order existingOrder = new Order(1L, 2L, 3L, Order.Status.Pending, new ArrayList<>());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        final int returnCode = orderService.updateOrderStatus(request);

        assertEquals(0, returnCode); // Constants.SUCCESS
//        verify(orderRepository, times(2)).findById(1L); // once inside update, once after
//        verify(orderRepository).save(any(Order.class));
    }

}
