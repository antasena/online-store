package com.example.onlinestore.controller;

import com.example.onlinestore.api.response.CustomerResponse;
import com.example.onlinestore.model.Customer;
import com.example.onlinestore.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public List<CustomerResponse> getAll() {
        List<CustomerResponse> customers = StreamSupport.stream(customerService.getAll().spliterator(), false)
                .map(item -> new CustomerResponse(item))
                .collect(Collectors.toList());
        return customers;
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable Long id) {
        return new CustomerResponse(customerService.findById(id));
    }
}
