package com.example.rasago.data.model

/**
 * Represents a single line item within an order, from the UI's perspective.
 *
 * This is used to display items in order summaries and for pre-populating
 * the database with dummy data.
 *
 * @param id The ID of the menu item this order item corresponds to.
 * @param name The name of the menu item.
 * @param price The price of a single unit of the item at the time the order was placed.
 * @param quantity The number of units of this item that were ordered.
 */
data class OrderItem(
    val id: Long,
    val name: String,
    val price: Double,
    val quantity: Int
)