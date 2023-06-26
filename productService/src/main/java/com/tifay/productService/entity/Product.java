package com.tifay.productService.entity;

import javax.persistence.Entity;

@Entity
public class Product {
    private long productId;
    private String productName;

    private long price;
    private long quantity;



}
