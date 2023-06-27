package com.talia.paymentService.controller;

import com.talia.paymentService.model.PaymentMode;
import com.talia.paymentService.model.PaymentRequest;
import com.talia.paymentService.model.PaymentResponse;
import com.talia.paymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {
        return  new ResponseEntity<>(paymentService.doPayment(paymentRequest), HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable long orderId) {
        return new ResponseEntity<>(paymentService.getPaymentDetailsByOrderId(String.valueOf(orderId)), HttpStatus.OK);

    }
}
