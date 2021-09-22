package com.example.onlinestore.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class OrderRequest {
    @NotNull
    private Long customerId;

    @NotNull
    private Long addressId;

    @NotNull
    @Size(min = 1, message = "Minimum one item should be ordered")
    private List<OrderItemRequest> items;
}
