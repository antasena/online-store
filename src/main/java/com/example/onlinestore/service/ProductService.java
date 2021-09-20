package com.example.onlinestore.service;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
public class ProductService {
    private ProductRepository productRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrementUnitInStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
        int updatedStcok = product.getUnitInStock() - quantity;
        if (updatedStcok > 0) {
            product.setUnitInStock(updatedStcok);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Cannot update stock in hand");
        }
    }
}
