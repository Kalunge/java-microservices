package com.tifay.orderService.service;

import com.tifay.orderService.entity.Order;
import com.tifay.orderService.exception.CustomException;
import com.tifay.orderService.external.client.PaymentService;
import com.tifay.orderService.external.client.ProductService;
import com.tifay.orderService.external.request.PaymentRequest;
import com.tifay.orderService.external.response.PaymentResponse;
import com.tifay.orderService.external.response.ProductResponse;
import com.tifay.orderService.model.OrderRequest;
import com.tifay.orderService.model.OrderResponse;
import com.tifay.orderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order Entity -> Save the data with Status Order Created
        //Product Service - Block Products (Reduce the Quantity)
        //Payment Service -> Payments -> Success-> COMPLETE, Else
        //CANCELLED

        log.info("Placing order request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating order with status created");

        Order order = Order.builder().amount(orderRequest.getAmount()).orderStatus("CREATED").productId(orderRequest.getProductId()).orderDate(Instant.now()).quantity(orderRequest.getQuantity()).build();

        order = orderRepository.save(order);

        log.info("Calling the paymentService to complete payment");

        PaymentRequest paymentRequest = PaymentRequest.builder().orderId(order.getId()).paymentMode(orderRequest.getPaymentMode()).orderAmount(orderRequest.getAmount()).build();


        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully");
            order.setOrderStatus("PLACED");
        } catch (Exception e) {
            log.error("Error occured in payment changing status to payment failed");
            order.setOrderStatus("PAYMENT FAILED");
        }

        order = orderRepository.save(order);


        log.info("Order placed successfully wth order id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for order id " + orderId);

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order Not found for the given id", "NOT_FOUND", 404));
        log.info("Invoking product service to get product for id");

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/products/" + order.getProductId(), ProductResponse.class);

        log.info("Getting payment information from payment service");

        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payments/orders/" + order.getId(), PaymentResponse.class);

        assert productResponse != null;
        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder().productName(productResponse.getProductName()).productId(productResponse.getProductId()).quantity(productResponse.getQuantity()).price(productResponse.getPrice()).build();

        assert paymentResponse != null;
        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .status(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        return OrderResponse.builder().orderId(order.getId())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
