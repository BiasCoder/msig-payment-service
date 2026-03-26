package com.test.msigpaymentservice.controller;

import com.test.msigpaymentservice.dto.PaymentRequestDTO;
import com.test.msigpaymentservice.model.Payment;
import com.test.msigpaymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create payment
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    // Callback simulation
    @PostMapping("/callback")
    public ResponseEntity<Payment> callback(
            @RequestParam String transactionId,
            @RequestParam String status) {

        return ResponseEntity.ok(paymentService.handleCallback(transactionId, status));
    }
}
