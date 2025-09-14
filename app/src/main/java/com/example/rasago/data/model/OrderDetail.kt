package com.example.rasago.data.model

/**
 * A data class that holds the complete details for a single order,
 * including the main order information and a list of all its items.
 */
data class OrderDetails(
    val order: Order,
    val items: List<OrderItem> // Now uses the detailed OrderItem model
)