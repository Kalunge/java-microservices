package com.tifay.productService.service;

import com.tifay.productService.model.ProductRequest;

public interface ProductService {
    long addProduct(ProductRequest productRequest);
}
