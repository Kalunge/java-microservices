package com.talia.paymentService.service;

import com.talia.paymentService.model.PaymentRequest;

public interface PaymentService {

    public long doPayment(PaymentRequest paymentRequest);
}
