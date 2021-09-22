package com.example.onlinestore.api.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Min(0)
    private int quantity;
}
