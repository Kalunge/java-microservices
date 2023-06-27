package com.talia.paymentService.service;

import com.talia.paymentService.model.PaymentRequest;
import com.talia.paymentService.model.PaymentResponse;

public interface PaymentService {

    public long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);

}
