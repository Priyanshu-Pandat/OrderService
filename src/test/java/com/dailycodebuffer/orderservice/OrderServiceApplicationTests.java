package com.dailycodebuffer.orderservice;

import com.dailycodebuffer.ProductService.model.ProductResponce;
import com.dailycodebuffer.orderservice.entity.Order;
import com.dailycodebuffer.orderservice.external.client.PaymentService;
import com.dailycodebuffer.orderservice.external.client.ProductService;
import com.dailycodebuffer.orderservice.external.responce.PaymentResponce;
import com.dailycodebuffer.orderservice.model.OrderResponce;
import com.dailycodebuffer.orderservice.model.PaymentMode;
import com.dailycodebuffer.orderservice.repository.OrderRepository;
import com.dailycodebuffer.orderservice.service.OrderService;
import com.dailycodebuffer.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.eclipse.jetty.http.HttpURI.build;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceApplicationTests {
     @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;


    @Mock
    private PaymentService paymentService;



    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    OrderService orderService = new OrderServiceImpl();
    @DisplayName("Get Order - Success Scenario")
    @Test
    void test_when_Order_Success(){

        // Mocking
        Order order = getMockObject();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                ProductResponce.class))
                .thenReturn(getMockProductResponse());

        when(paymentService.getPaymentDetailsByOrderId(order.getOrderId()))
                .thenReturn(ResponseEntity.ok(getMockPaymentResponse()));

        // Actual
        OrderResponce orderResponce = orderService.getOrderDetails(1);

        // Verification
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate , times(1)).getForObject(
                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                ProductResponce.class);
        verify(paymentService , times(1))
                .getPaymentDetailsByOrderId(order.getOrderId());

        // Assert
        assertNotNull(orderResponce);
        assertEquals(order.getOrderId(), orderResponce.getOrderId());
    }

    private PaymentResponce getMockPaymentResponse() {
        return PaymentResponce.builder()
               .paymentId(1)
               .paymentMode(PaymentMode.CASH)
                .orderId(1)
                .status("Accepted")
               .amount(100)
               .paymentDate(Instant.now())

               .build();
    }

    private ProductResponce getMockProductResponse() {
        return ProductResponce.builder()
                .productId(2)
                .productName("iphone")
                .price(100)
                .quantity(29)
                .build();
    }

    private Order getMockObject() {
        return Order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .orderId(1)
                .amount(100)
                .productId(2)
                .quantity(27)
                .build();
    }


}
