package com.tifay.orderService.external.request;

import com.tifay.orderService.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private long orderId;
    private long orderAmount;
    private String referenceNumber;

    private PaymentMode paymentMode;
}
