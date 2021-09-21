package com.example.onlinestore.repository;

import com.example.onlinestore.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT o from Order o WHERE o.id = :id and o.recordStatus = 1")
    Optional<Order> findActiveOrderById(Long id);
}
