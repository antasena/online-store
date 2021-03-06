package com.example.onlinestore.api.response;

import com.example.onlinestore.model.Orders;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {
    private Long orderId;
    private CustomerResponse customer;
    private AddressResponse deliveryAddress;
    private String orderStatus;
    private BigDecimal totalAmount;
    private Date orderDate;
    private List<OrderItemResponse> items = new ArrayList<>();

    public OrderResponse(Orders orders) {
        this.orderId = orders.getId();
        this.orderStatus = orders.getStatus();
        this.customer = new CustomerResponse(orders.getCustomer());
        this.deliveryAddress = new AddressResponse(orders.getAddress());
        this.totalAmount = orders.getTotalAmount();
        this.orderDate = orders.getOrderDate();
        this.items = orders.getOrderItems().stream().map(
                item -> new OrderItemResponse(item)
        ).collect(Collectors.toList());
    }
}
