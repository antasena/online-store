package com.example.onlinestore.service;

import com.example.onlinestore.api.request.OrderItemRequest;
import com.example.onlinestore.api.request.OrderRequest;
import com.example.onlinestore.exceptionhandling.BusinessValidationError;
import com.example.onlinestore.exceptionhandling.RecordNotFoundException;
import com.example.onlinestore.model.*;
import com.example.onlinestore.repository.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrdersService {
    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    public Orders processOrder(OrderRequest orderRequest) {
        Customer customer = customerService.findById(orderRequest.getCustomerId());
        Address deliveryAddress = addressService.findById(orderRequest.getAddressId());
        List<OrderItem> orderItems = constructOrderItems(orderRequest);

        return processOrder(orderItems, customer, deliveryAddress);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Orders processOrder(List<OrderItem> orderItems, Customer customer, Address deliveryAddress) {
        try {
            log.debug("Processing order..");
            //validate order item
            validateOrderItem(orderItems);

            //construct orders details
            Orders orders = new Orders();
            orders.setTotalAmount(calculateTotalAmount(orderItems));
            orders.setStatus("New");
            orders.setOrderDate(new Date());
            orders.setAddress(deliveryAddress);
            orders.setCustomer(customer);
            orders.setOrderItems(orderItems);

            //update product's unit in stock
            updateProductUnitInStock(orderItems);

            //all good, save orders
            Long orderId = saveOrder(orders);
            orders.setId(orderId);

            log.debug("Done processing order..");
            return orders;
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Cannot process order, the product lock by other process. Retrying the process..");
            return processOrder(orderItems, customer, deliveryAddress);
        }
    }

    @Transactional
    public Long saveOrder(Orders orders) {
        log.debug("Saving order");
        if (orders.getId() == null) {
            orders.setCreatedDate(new Date());
        }
        orders.setLastUpdatedDate(new Date());

        return ordersRepository.save(orders).getId();
    }

    @Transactional(readOnly = true)
    public Orders findById(Long orderId) {
        return ordersRepository.findById(orderId).orElseThrow(() -> new RecordNotFoundException(Orders.class, orderId));
    }

    @Transactional(readOnly = true)
    public Iterable<Orders> getAll() {
        return ordersRepository.findAll();
    }

    private void validateOrderItem(List<OrderItem> items) {
        items.stream().forEach(item -> {
                    Product product = productService.findById(item.getProduct().getId());
                    log.debug(String.format("Validating order item for productId: '%d' with requested quantity: '%d'", product.getId(), item.getQuantity()));
                    int updatedStcok = product.getUnitInStock() - item.getQuantity();
                    if (updatedStcok < 0) {
                        String error = String.format("Invalid requested quantity: '%d' for productId: '%s', its greater than available stock: '%d'.",
                                item.getQuantity(), product.getId(), product.getUnitInStock());
                        log.error(error);
                        throw new BusinessValidationError(error);
                    }
                }
        );
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        log.debug("Calculating total amount");
        BigDecimal total = new BigDecimal(0);
        for (OrderItem item : items) {
            total = total.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private void updateProductUnitInStock(List<OrderItem> items) {
        items.stream().forEach(item ->
                productService.reduceUnitInStock(item.getProduct().getId(), item.getQuantity())
        );
    }

    private List<OrderItem> constructOrderItems(OrderRequest orderRequest) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getItems()) {
            Product product = productService.findById(orderItemRequest.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
