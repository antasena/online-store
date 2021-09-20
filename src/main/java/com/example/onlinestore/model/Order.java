package com.example.onlinestore.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
    private Long id;
    private String status;
    private Date orderDate;
    private Customer customer;
    private Address deliveryAddress;
    private BigDecimal totalAmount;
    private List<Item> items;
}
