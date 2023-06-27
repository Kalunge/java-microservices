package com.talia.paymentService.service;

import com.talia.paymentService.entity.TransactionDetails;
import com.talia.paymentService.model.PaymentRequest;
import com.talia.paymentService.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImplementation implements PaymentService {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording payment details: {}", paymentRequest);

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("success")
              .orderId(paymentRequest.getOrderId()).referenceNumber(paymentRequest.getReferenceNumber()).amount(paymentRequest.getOrderAmount()).build();

        transactionDetailsRepository.save(transactionDetails);

        log.info("Transaction completed with id " + transactionDetails.getId());

        return transactionDetails.getId();
    }
}
