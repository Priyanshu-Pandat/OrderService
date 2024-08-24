package com.dailycodebuffer.orderservice.entity;

import com.dailycodebuffer.orderservice.model.PaymentMode;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ORDER_DETAILS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;
    @Column(name = "PRODUCT_ID")

    private long productId;
    @Column(name = "ORDER_STATUS")
    private String orderStatus;
    @Column(name = "QUANTITY")
    private long quantity;
    @Column(name = "ORDER_DATE")
    private Instant orderDate;
    @Column(name = "TOTAL_AMOUNT")
    private long amount;

}
