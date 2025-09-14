package com.example.rasago.order

import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.model.Order

/**
 * Represents the state of the user's current order for the UI.
 *
 * It is a data class, which is crucial for Jetpack Compose to detect state changes
 * and trigger UI recomposition automatically.
 *
 * @property order The basic details of the order, like order number and time.
 * @property orderItems The list of menu items currently in the cart.
 * @property subtotal The total price of all items before tax and service charge.
 * @property serviceCharge A fixed or calculated service charge for the order.
 * @property tax The calculated tax amount for the current subtotal.
 * @property total The final price, including subtotal, service charge, and tax.
 */
data class OrderUiState(
    val order: Order? = null,
    val orderItems: List<MenuItem> = emptyList(),
    val subtotal: Double = 0.0,
    val serviceCharge: Double = 0.0, // Added service charge
    val tax: Double = 0.0,
    val total: Double = 0.0
)