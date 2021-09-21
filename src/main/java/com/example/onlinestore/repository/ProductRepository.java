package com.example.onlinestore.repository;

import com.example.onlinestore.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p from Product p WHERE p.id = :id and p.recordStatus = 1")
    Optional<Product> findActiveProductById(Long id);
}
