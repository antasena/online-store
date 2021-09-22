package com.example.onlinestore.controller;

import com.example.onlinestore.api.request.OrderRequest;
import com.example.onlinestore.api.response.OrderResponse;
import com.example.onlinestore.model.Orders;
import com.example.onlinestore.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;


    @PostMapping("/process")
    public OrderResponse processOrders(@Valid @RequestBody OrderRequest orderRequest) {
        Orders order = ordersService.processOrder(orderRequest);
        return new OrderResponse(order);
    }

    @GetMapping("")
    public List<OrderResponse> getAllOrders() {
        List<OrderResponse> orders = StreamSupport.stream(ordersService.getAll().spliterator(), false)
                .map(item -> new OrderResponse(item))
                .collect(Collectors.toList());
        return orders;
    }
}
