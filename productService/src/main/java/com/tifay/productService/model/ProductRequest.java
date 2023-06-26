package com.tifay.productService.model;

import lombok.Data;

@Data // getters and setters generator
public class ProductRequest {
    private String name;
    private  long price;
    private long quantity;
}
