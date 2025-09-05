package com.example.rentit.domain.product.model

sealed class PaymentValidationResult {
    data object Success : PaymentValidationResult()
    data object NotRenter : PaymentValidationResult()
    data object InvalidStatus : PaymentValidationResult()
    data object ProductNotFound : PaymentValidationResult()
}