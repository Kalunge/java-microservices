package com.tifay.productService.service;

import com.tifay.productService.model.ProductRequest;
import com.tifay.productService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);
    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
