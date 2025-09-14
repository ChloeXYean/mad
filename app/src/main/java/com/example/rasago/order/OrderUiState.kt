package com.example.rasago.order

import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderItem

data class OrderUiState(
    val orders: List<Order> = emptyList(),
    val cartItems: List<OrderItem> = emptyList(),
    val selectedOrder: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)