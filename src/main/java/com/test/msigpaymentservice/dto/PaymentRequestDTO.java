package com.test.msigpaymentservice.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String transactionId;
    private Double amount;
}
