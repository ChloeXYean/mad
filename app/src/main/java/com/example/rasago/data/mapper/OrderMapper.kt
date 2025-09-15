
package com.example.rasago.data.mapper

import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.model.Order

/**
 * Converts an OrderEntity from the database into an Order model for the app's UI.
 */
fun OrderEntity.toOrder(): Order {
    return Order(
        orderId = this.orderId,
        orderNo = this.orderNo,
        orderTime = this.orderTime,
        subtotal = this.subtotal,
        serviceCharge = this.serviceCharge,
        sst = this.sst,
        totalPayment = this.totalPayment,
        paymentMethod = this.paymentMethod,
        remarks = this.remarks,
        orderType = this.orderType,
        foodStatus = this.foodStatus,
        customerId = this.customerId
    )
}
