package com.example.onlinestore.controller;

import com.example.onlinestore.api.request.ProductRequest;
import com.example.onlinestore.api.response.ProductResponse;
import com.example.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ProductResponse> getAllOProducts() {
        List<ProductResponse> products = StreamSupport.stream(productService.getAll().spliterator(), false)
                .map(item -> new ProductResponse(item))
                .collect(Collectors.toList());
        return products;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProductResponse findById(@PathVariable Long id) {
        return new ProductResponse(productService.findById(id));
    }

    @RequestMapping(value = "/addStock", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addUnitInStock(@RequestBody @Valid ProductRequest productRequest) {
        productService.addUnitInStock(productRequest.getProductId(), productRequest.getQuantity());
    }
}
