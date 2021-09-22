package com.example.onlinestore.api.response;

import com.example.onlinestore.model.Product;
import lombok.Data;

@Data
public class ProductResponse {
    Long productId;
    String name;
    String description;
    int unitInStock;

    public ProductResponse(Product product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.unitInStock = product.getUnitInStock();
    }
}
