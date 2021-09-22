package com.example.onlinestore.service;

import com.example.onlinestore.api.request.OrderItemRequest;
import com.example.onlinestore.api.request.OrderRequest;
import com.example.onlinestore.model.*;
import com.example.onlinestore.repository.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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
        Set<OrderItem> orderItems = constructOrderItems(orderRequest);

        return processOrder(orderItems, customer, deliveryAddress);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Orders processOrder(Set<OrderItem> orderItems, Customer customer, Address deliveryAddress) {
        try {
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

            //save orders
            Long orderId = saveOrder(orders);
            orders.setId(orderId);

            //update product's unit in stock
            updateProductUnitInStock(orderItems);

            return orders;
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Cannot process order, the stock quantity updated by others. Please try again.", e);
            throw e;
        } catch (ValidationException e) {
            log.error("Cannot process order, invalid order.", e);
            throw e;
        }
    }

    @Transactional
    public Long saveOrder(Orders orders) {
        if (orders.getId() == null) {
            orders.setCreatedDate(new Date());
        }
        orders.setLastUpdatedDate(new Date());

        return ordersRepository.save(orders).getId();
    }

    @Transactional(readOnly = true)
    private Orders findOrderById(Long orderId) {
        return ordersRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Iterable<Orders> getAll() {
        return ordersRepository.findAll();
    }

    private void validateOrderItem(Set<OrderItem> items) {
        items.stream().forEach(item -> {
                    Product product = productService.findById(item.getProduct().getId());
                    int updatedStcok = product.getUnitInStock() - item.getQuantity();
                    if (updatedStcok < 0) {
                        throw new ValidationException("Invalid requested quantity, product's unit in stock is not enough for order.");
                    }
                }
        );
    }

    private BigDecimal calculateTotalAmount(Set<OrderItem> items) {
        BigDecimal total = new BigDecimal(0);
        items.stream().forEach(item ->
                total.add(item.getProduct().getPrice().multiply(
                        new BigDecimal(item.getQuantity()))
                )
        );
        return total;
    }

    private void updateProductUnitInStock(Set<OrderItem> items) {
        items.stream().forEach(item ->
                productService.decrementUnitInStock(item.getProduct().getId(), item.getQuantity())
        );
    }

    private Set<OrderItem> constructOrderItems(OrderRequest orderRequest) {
        Set<OrderItem> orderItems = new LinkedHashSet<>();
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
