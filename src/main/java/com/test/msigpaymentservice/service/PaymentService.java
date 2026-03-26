package com.test.msigpaymentservice.service;

import com.test.msigpaymentservice.dto.PaymentRequestDTO;
import com.test.msigpaymentservice.model.Payment;
import com.test.msigpaymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

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
            return payment; // ignore duplicate callback
        }

        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}
