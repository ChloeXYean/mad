//package com.example.rasago.data.mapper
//
//import com.example.rasago.data.entity.OrderEntity
//import com.example.rasago.data.entity.OrderItemEntity
//import com.example.rasago.data.model.Order
//import com.example.rasago.data.model.OrderItem
//import com.example.rasago.data.model.OrderWithItems
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//fun OrderEntity.toOrder(items: List<OrderItem>): Order {
//    return Order(
//        no = orderNo,
//        type = orderType,
//        time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(orderTime), //? .format(orderTime) also can
//        status = foodStatus,
//        orderItems = items
//    )
//}
//
//fun Order.toEntity(): OrderEntity {
//    return OrderEntity(
//        orderNo = no,
//        orderTime = System.currentTimeMillis(),
//        subtotal = subtotal,
//        sst = subtotal * 0.06,
//        totalPayment = subtotal + (subtotal * 0.06),
//        orderType = type,
//        foodStatus = status,
//        serviceCharge = 0.0,     // TODO: make dynamic later
//        paymentMethod = "Unknown", // TODO: make dynamic later
//        remarks = null           // TODO: allow UI to set remarks
//    )
//}
//
//fun OrderWithItems.toOrder(): Order {
//    return Order(
//        no = order.orderNo,
//        type = order.orderType,
//        time = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(order.orderTime),
//        status = order.foodStatus,
//        orderItems = items.map { entity ->
//            OrderItem(
//                id = entity.id,
//                name = entity.menuItemName,
//                price = entity.price,
//                quantity = entity.quantity,
//                status = "Preparing"
//            )
//        }
//    )
//}
//
//fun OrderItem.toEntity(orderId: Long): OrderItemEntity {
//    return OrderItemEntity(
//        orderId = orderId,
//        menuItemName = this.name,
//        price = this.price,
//        quantity = this.quantity
//    )
//}
//
package com.example.rasago.data.mapper

import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.model.Order

/**
 * Converts an OrderEntity from the database into an Order model for the app's UI.
 */
fun OrderEntity.toOrder(): Order {
    return Order(
        id = this.orderId,
        orderNo = this.orderNo,
        orderTime = this.orderTime,
        status = this.foodStatus
        // Note: The list of items is handled separately in the OrderRepository
        // to build the complete OrderDetails object.
    )
}
