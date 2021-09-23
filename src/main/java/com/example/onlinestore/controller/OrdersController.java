package com.example.onlinestore.controller;

import com.example.onlinestore.api.request.OrderRequest;
import com.example.onlinestore.api.response.OrderResponse;
import com.example.onlinestore.model.Orders;
import com.example.onlinestore.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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


    @RequestMapping(value = "/process", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public OrderResponse processOrders(@Valid @RequestBody OrderRequest orderRequest) {
        Orders order = ordersService.processOrder(orderRequest);
        return new OrderResponse(order);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<OrderResponse> getAllOrders() {
        List<OrderResponse> orders = StreamSupport.stream(ordersService.getAll().spliterator(), false)
                .map(item -> new OrderResponse(item))
                .collect(Collectors.toList());
        return orders;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public OrderResponse findById(@PathVariable Long id) {
        return new OrderResponse(ordersService.findById(id));
    }
}
