package com.test.msigpaymentservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(columnNames = "transactionId"))
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String status; // PENDING, SUCCESS, FAILED
    private Double amount;

    // getter setter
}