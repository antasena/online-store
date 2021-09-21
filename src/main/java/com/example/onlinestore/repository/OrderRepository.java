package com.example.onlinestore.repository;

import com.example.onlinestore.model.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Orders, Long> {

    @Query("SELECT o from Orders o WHERE o.id = :id and o.recordStatus = 1")
    Optional<Orders> findActiveOrderById(Long id);
}
