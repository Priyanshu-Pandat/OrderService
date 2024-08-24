package com.dailycodebuffer.orderservice.controller;

import com.dailycodebuffer.orderservice.model.OrderRequest;
import com.dailycodebuffer.orderservice.model.OrderResponce;
import com.dailycodebuffer.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {
    @Autowired
   private OrderService orderService;

    // ** placing the order ***** //

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {
        long orderID = orderService.placeOrder(orderRequest);
        log.info("Order ID: {} " , orderID);
        return new  ResponseEntity<>(orderID , HttpStatus.OK);
    }

    // *** Get the order By OrderId **** //

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponce> getOrderDetails( @PathVariable long orderId) {
        OrderResponce orderResponce =
                orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(orderResponce, HttpStatus.OK);
    }
}
