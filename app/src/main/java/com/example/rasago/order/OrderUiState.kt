package com.example.rasago.order

import com.example.rasago.data.model.CartItem

data class OrderUiState(
    val orderItems: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val serviceCharge: Double = 0.0,
    val tax: Double = 0.0,
    val takeAwayCharge: Double = 0.0,
    val total: Double = 0.0,
    val orderType: String = "Dine-In",
    val paymentMethod: String = "QR Scan" // Added to manage payment method
)

