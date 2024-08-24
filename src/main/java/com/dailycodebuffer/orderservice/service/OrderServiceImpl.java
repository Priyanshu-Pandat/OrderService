package com.dailycodebuffer.orderservice.service;

import com.dailycodebuffer.ProductService.model.ProductResponce;
import com.dailycodebuffer.orderservice.entity.Order;
import com.dailycodebuffer.orderservice.exception.CustomException;
import com.dailycodebuffer.orderservice.external.client.PaymentService;
import com.dailycodebuffer.orderservice.external.client.ProductService;
import com.dailycodebuffer.orderservice.external.request.PaymentRequest;
import com.dailycodebuffer.orderservice.external.responce.PaymentResponce;
import com.dailycodebuffer.orderservice.model.OrderRequest;
import com.dailycodebuffer.orderservice.model.OrderResponce;
import com.dailycodebuffer.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    // Object of the productService ;
    @Autowired
    private ProductService productService;

    // Object of the PaymentService
    @Autowired
    private PaymentService paymentService;

    // Object of the RestTemplate
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order Entity -> Save the Data with Status Order Created   (DONE)v/
        //Product Service -> Block Products (Reduce the Quantity)    (DONE)v/
        //Payment Service -> Payment -> Success -> COMPLETED , Else  (DONE)v/
        // CANCELLED
        log.info("Placing Order Request:{} " , orderRequest);

        // ******** Reduce Quantity in Product Service   *******
        // calling Product API form the Order API (Feign Client Method)

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status Created ");
        Order order =  Order.builder()
                    .amount(orderRequest.getTotalAmount())
                    .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
       order =   orderRepository.save(order);


//**************************************************************************************************//
       // *** ab Payment krne ke liye Payment Service call krte hai ******* //
// ***********        this is Feign Client Method for calling the Payment Service // **************
        log.info("Calling PAYMENT-SERVICE to Complete the Payment");

        PaymentRequest paymentRequest =
                PaymentRequest.builder()
                        .orderId(order.getOrderId())
                        .paymentMode(orderRequest.getPaymentMode())
                        .amount(orderRequest.getTotalAmount())
                        .build();
        String orderStatus = null;
        try {
            paymentService.dopayment(paymentRequest);
            log.info("Payment done SuccessFully . Changing the Order Status ");
            orderStatus = "PLACED";
        } catch (Exception  e) {
            log.error("Payment Failed due do some Error . Changing  the Order Status ");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Oder Placed Successfully with Order ID:{} " , order.getOrderId());

        return order.getOrderId();
    }

//**************************************************************************//
           ////** // *** get the orderDetails from DB *** ///  ///**///
    @Override
    public OrderResponce getOrderDetails(long orderId) {
        Order order
                = orderRepository.findById(orderId)
                       .orElseThrow(() -> new CustomException("Order not found for this id:" + orderId ,
                               "NOT_FOUND" ,
                               404));


//**********************************************************************************//
        // **** this is RestTemplate Method for calling the Product api **** //
        log.info("Invoking Product Service to fetch the Product Id :{}",order.getProductId());
        ProductResponce productResponce
                =restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                        ProductResponce.class);


        log.info("Getting Payment Information fomr the Payment-Service");
      //**********************************************************************//
// ** this is Feign Client Method for get information **// this is my own method

        PaymentResponce paymentResponce = paymentService.getPaymentDetailsByOrderId(order.getOrderId()).getBody();

        // ab payment response me se jo payment details aii h unhe  paymentdetails(Class) me save krte hai
        OrderResponce.PaymentDetails paymentDetails
             = OrderResponce.PaymentDetails.builder()
                .paymentId(paymentResponce.getPaymentId())
                .paymentMode(paymentResponce.getPaymentMode())
                .paymentDate(paymentResponce.getPaymentDate())
                .paymentStatus(paymentResponce.getStatus())
                .build();

        OrderResponce.ProductDetails productDetails
                =OrderResponce.ProductDetails.builder()
                .productName(productResponce.getProductName())
                .productId(productResponce.getProductId())
                .quantity(productResponce.getQuantity())
                .price(productResponce.getPrice())
                .build();


        log.info("Order Details :{}", order);
        OrderResponce orderResponce =
                OrderResponce.builder()
                        .orderId(order.getOrderId())
                        .amount(order.getAmount())
                        .orderDate(order.getOrderDate())
                        .orderStatus(order.getOrderStatus())
                        .productDetails(productDetails)
                        .paymentDetails(paymentDetails)
                        .build();
                return orderResponce;
    }

}
