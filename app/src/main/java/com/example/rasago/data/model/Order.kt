package com.example.rasago.data.model

//App logic and UI
data class OrderItem(
    val name: String,
    val price: Double,
    val quantity: Int,
    var status: String = "Preparing"
)

data class Order(
    val no: String,          // Order number
    var type: String,        // Dine-In or Takeaway
    val time: String,        // Order time as string
    var status: String,      // Preparing, Done, Cancelled
    val orderItems: List<OrderItem> = emptyList()
) {
    val subtotal: Double
        get() = orderItems.sumOf { it.price * it.quantity }
}
