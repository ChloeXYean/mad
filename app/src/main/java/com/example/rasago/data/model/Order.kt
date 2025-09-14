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

data class Order(
    val orderId: Int,
    val orderNo: String,
    val orderTime: String,
    val subtotal: Double,
    val serviceCharge: Double,
    val sst: Double,
    val totalPayment: Double,
    val paymentMethod: String,
    val remarks: String?,
    val orderType: String,
    val foodStatus: String,
    val customerId: Int
)