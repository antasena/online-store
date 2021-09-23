package com.example.onlinestore.service;

import com.example.onlinestore.exceptionhandling.RecordNotFoundException;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reduceUnitInStock(Long productId, int quantity) {
        log.debug(String.format("Reduce unit in stock for productId: '%s' by value: '%d'", productId, quantity));
        Product product = productRepository.findActiveProductById(productId).orElseThrow(EntityNotFoundException::new);
        int updatedStcok = product.getUnitInStock() - quantity;
        product.setUnitInStock(updatedStcok);
        saveProduct(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long productId) {
        return productRepository.findActiveProductById(productId).orElseThrow(() -> new RecordNotFoundException(Product.class, productId));
    }

    @Transactional(readOnly = true)
    public Iterable<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Long saveProduct(Product product) {
        log.debug("Saving product");
        if (product.getId() == null) {
            product.setCreatedDate(new Date());
        }
        product.setLastUpdatedDate(new Date());
        Product newProduct = productRepository.save(product);
        return newProduct.getId();
    }

    @Transactional
    public void deleteProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RecordNotFoundException(Product.class, productId));
        deleteProduct(product);
    }

    @Transactional
    public void deleteProduct(Product product) {
        log.debug("Deleting product: ", product.getId());
        product.setRecordStatus(0);
        product.setLastUpdatedDate(new Date());
        saveProduct(product);
    }
}
