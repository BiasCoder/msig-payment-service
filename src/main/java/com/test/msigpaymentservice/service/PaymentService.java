package com.test.msigpaymentservice.service;

import com.test.msigpaymentservice.dto.PaymentRequestDTO;
import com.test.msigpaymentservice.model.Payment;
import com.test.msigpaymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Payment createPayment(PaymentRequestDTO request) {
        Optional<Payment> existing = paymentRepository.findByTransactionId(request.getTransactionId());

        if (existing.isPresent()) {
            return existing.get(); // idempotent (no duplicate)
        }

        Payment payment = new Payment();
        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        payment.setStatus("PENDING");

        return paymentRepository.save(payment);
    }

    public Payment handleCallback(String transactionId, String status) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // IMPORTANT: prevent double update
        if ("SUCCESS".equals(payment.getStatus())) {
            // Update Order
            String orderUrl = "http://localhost:8082/api/orders/callback?transactionId="
                    + transactionId + "&status=SUCCESS";
            restTemplate.postForObject(orderUrl, null, String.class);

            // Send Notification
            String notifUrl = "http://localhost:8083/api/notifications?transactionId="
                    + transactionId + "&status=SUCCESS";
            restTemplate.postForObject(notifUrl, null, String.class);
        }

        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}
