package com.tifay.productService.service;

import com.tifay.productService.entity.ProductEntity;
import com.tifay.productService.exception.ProductServiceCustomException;
import com.tifay.productService.model.ProductRequest;
import com.tifay.productService.model.ProductResponse;
import com.tifay.productService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Id;

@Service
@Log4j2
public class ProductServiceImplementation implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product");

        ProductEntity product = ProductEntity.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product created");

        return product.getProductId();


    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productId: {}", productId);
        ProductEntity entity = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("Product with the given id could not be found ", "PRODUCT_NOT_FOUND"));
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(entity, productResponse);

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("`reduce quantity: {} for id: {}", quantity, productId);

        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("Product with given id not found", "PRODUCT_NOT_FOUND"));
        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Product does not have sufficient quantity", "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        log.info("Product quantity successfully updated");
    }
}
