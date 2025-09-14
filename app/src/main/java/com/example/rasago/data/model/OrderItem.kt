package com.example.rasago.data.model

/**
 * Represents a single line item within an order, from the UI's perspective.
 *
 * This combines data from the MenuItem and OrderItem entities for display.
 */
data class OrderItem(
    val id: Int, // This is the OrderItemEntity id
    val menuItemId: Int?,
    val name: String,
    val photo: String,
    val price: Double,
    val quantity: Int,
    val status: String // Status is now part of the UI model
)

