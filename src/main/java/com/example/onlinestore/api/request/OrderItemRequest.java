package com.example.onlinestore.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private int quantity;
}
