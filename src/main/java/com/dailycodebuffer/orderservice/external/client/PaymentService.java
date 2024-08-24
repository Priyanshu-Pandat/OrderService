package com.dailycodebuffer.orderservice.external.client;

import com.dailycodebuffer.orderservice.exception.CustomException;
import com.dailycodebuffer.orderservice.external.request.PaymentRequest;
import com.dailycodebuffer.orderservice.external.responce.PaymentResponce;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")

public interface PaymentService {
    @PostMapping()
    public ResponseEntity<Long> dopayment(@RequestBody PaymentRequest paymentRequest);


    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponce> getPaymentDetailsByOrderId(@PathVariable long orderId);

    default void fallback(Exception e) {
        throw  new CustomException("Payment Service is not available" , "UNAVAILABLE", 500);
    }
}
