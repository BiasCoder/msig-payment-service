# Payment Service

## Overview
Payment Service is responsible for handling payment processing, including ensuring idempotency and preventing double charge scenarios.

## Features
- Create payment transaction
- Handle payment gateway callback
- Prevent duplicate transaction (idempotent)
- Prevent double charge
- Update transaction status

## Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)

## Port
8081 (local)

## API Endpoints

### Create Payment
POST /api/payments

### Callback payment
POST /api/payments/callback

## Query Params
transactionId
status

## Key Concepts Implemented
### Idempotency
Transaction is uniquely identified using transactionId
Duplicate requests will not create new records
### Duplicate Callback Handling
If payment is already SUCCESS, further callbacks will be ignored
### No Double Charge
System prevents multiple updates once transaction is completed

## Database
Using H2 in-memory database
Data will reset on application restart
