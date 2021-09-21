package com.example.onlinestore.service;

import com.example.onlinestore.model.Orders;
import com.example.onlinestore.model.OrderItem;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class OrderService {
    private ProductService productService;
    private OrderRepository orderRepository;

    @Transactional
    public Long processOrder(Orders orders) {
        try {
            //validate order item
            validateOrderItem(orders.getOrderItems());

            orders.setTotalAmount(calculateTotalAmount(orders.getOrderItems()));
            orders.setStatus("New");
            orders.setOrderDate(new Date());
            Long orderId = saveOrder(orders);

            //update product's unit in stock
            updateProductUnitInStock(orders.getOrderItems());

            return orderId;
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

        return orderRepository.save(orders).getId();
    }

    @Transactional(readOnly = true)
    private Orders findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
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
}
