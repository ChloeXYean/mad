package com.example.rasago.data.model

/**
 * Represents a fully processed order, containing the main order details
 * and a list of detailed menu items with their correct quantities.
 */
data class OrderDetails(
    val order: Order,
    val items: List<MenuItem>
)
