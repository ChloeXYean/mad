//package com.example.rasago.data.model
//
////App logic and UI
//data class OrderItem(
//    val id: Long, // unique identifier
//    val name: String,
//    val price: Double,
//    val quantity: Int,
//    var status: String = "Preparing"
//)
//
//data class Order(
//    val no: String,          // Order number
//    var type: String,        // Dine-In or Takeaway
//    val time: String,        // Order time as string
//    var status: String,      // Preparing, Done, Cancelled
//    val orderItems: List<OrderItem>,
//    val customerId: String
//) {
//    val subtotal: Double
//        get() = orderItems.sumOf { it.price * it.quantity }
//}
package com.example.rasago.data.model

/**
 * Represents the core details of a customer's order.
 *
 * This data class holds the metadata for an order, such as its unique number and status.
 * The actual items within the order are managed separately in the OrderUiState to keep
 * the active cart flexible.
 *
 * @param id The unique identifier for the order in the database.
 * @param orderNo The user-facing order number (e.g., "T01").
 * @param orderTime The time the order was placed.
 * @param status The current status of the order (e.g., "Pending", "Preparing").
 */
data class Order(
    val id: Long,
    val orderNo: String,
    val orderTime: String,
    val status: String
)