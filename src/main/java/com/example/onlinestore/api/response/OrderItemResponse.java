package com.example.onlinestore.api.response;

import com.example.onlinestore.model.OrderItem;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Long orderItemId;
    private String productName;
    private int quantity;

    public OrderItemResponse(OrderItem orderItem) {
        this.orderItemId = orderItem.getId();
        this.productName = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
    }
}
