package com.example.onlinestore.service;

import com.example.onlinestore.model.Order;
import com.example.onlinestore.model.OrderItem;
import com.example.onlinestore.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class OrderService {
    private ProductService productService;
    private OrderRepository orderRepository;

    @Transactional
    public Long createOrder(Order order) {
        try {
            order.setTotalAmount(calculateTotalAmount(order.getOrderItems()));
            order.setStatus("New");
            order.setOrderDate(new Date());

            updateProductStock(order.getOrderItems());

            Order newOrder = orderRepository.save(order);
            return newOrder.getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Cannot create order, the stock quantity updated by others. Please try again");
            return 0L;
        }
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

    private void updateProductStock(Set<OrderItem> items) {
        items.stream().forEach(item ->
                productService.decrementUnitInStock(item.getProduct().getId(), item.getQuantity())
        );
    }
}
