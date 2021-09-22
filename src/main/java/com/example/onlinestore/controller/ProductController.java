package com.example.onlinestore.controller;

import com.example.onlinestore.api.response.ProductResponse;
import com.example.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public List<ProductResponse> getAllOProducts() {
        List<ProductResponse> products = StreamSupport.stream(productService.getAll().spliterator(), false)
                .map(item -> new ProductResponse(item))
                .collect(Collectors.toList());
        return products;
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return new ProductResponse(productService.findById(id));
    }
}
