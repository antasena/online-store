package com.example.onlinestore.service;

import com.example.onlinestore.model.Orders;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrementUnitInStock(Long productId, int quantity) {
        Product product = productRepository.findActiveProductById(productId).orElseThrow(EntityNotFoundException::new);
        int updatedStcok = product.getUnitInStock() - quantity;
        product.setUnitInStock(updatedStcok);
        saveProduct(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long productId) {
        return productRepository.findActiveProductById(productId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Iterable<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Long saveProduct(Product product) {
        if (product.getId() == null) {
            product.setCreatedDate(new Date());
        }
        product.setLastUpdatedDate(new Date());
        Product newProduct = productRepository.save(product);
        return newProduct.getId();
    }

    @Transactional
    public void deleteProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
        deleteProduct(product);
    }

    @Transactional
    public void deleteProduct(Product product) {
        product.setRecordStatus(0);
        saveProduct(product);
    }
}
